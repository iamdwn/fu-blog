package tech.fublog.FuBlog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.request.RequestBlogPostDTO;
import tech.fublog.FuBlog.entity.*;
import tech.fublog.FuBlog.exception.PostTagException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.CategoryRepository;
import tech.fublog.FuBlog.repository.TagRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tech.fublog.FuBlog.exception.BlogPostException;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final TagRepository tagRepository;

    @Autowired
    public BlogPostService(CategoryRepository categoryRepository, UserRepository userRepository, BlogPostRepository blogPostRepository, TagRepository tagRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
        this.tagRepository = tagRepository;
    }


    public BlogPostEntity getBlogById(Long postId) {

        return blogPostRepository.findById(postId).orElse(null);
    }

    public BlogPostDTO getBlogPostById(Long postId) {

        BlogPostEntity blogPostEntity = blogPostRepository.findById(postId).orElse(null);

        if (blogPostEntity != null) {
            blogPostEntity.setView(blogPostEntity.getView());
            blogPostRepository.save(blogPostEntity);
            UserEntity userEntity = userRepository.findById(blogPostEntity.getAuthors().getId()).orElse(null);

            Set<RoleEntity> roleEntities = userEntity.getRoles();
            Set<PostTagEntity> postTagEntity = blogPostEntity.getPostTags();
            Set<TagDTO> tagDTOs = postTagEntity.stream()
                    .map(tagEntity -> {
                        TagDTO tagDTO = new TagDTO();
                        tagDTO.setTagId(tagEntity.getId());
                        tagDTO.setTagName(tagEntity.getTag().getTagName());
                        return tagDTO;
                    })
                    .collect(Collectors.toSet());

            List<String> roleNames = roleEntities.stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toList());

            UserDTO userDTO = new UserDTO(userEntity.getFullName(),
                    userEntity.getPassword(),
                    userEntity.getEmail(),
                    userEntity.getId(),
                    userEntity.getPicture(),
                    userEntity.getStatus(),
                    roleNames.get(roleNames.size()-1),
                    roleNames);

            BlogPostDTO blogPostDTO = new BlogPostDTO(blogPostEntity.getId(),
                    blogPostEntity.getTypePost(),
                    blogPostEntity.getTitle(),
                    blogPostEntity.getContent(),
                    blogPostEntity.getCategory().getCategoryName(),
                    blogPostEntity.getCategory().getParentCategory(),
                    tagDTOs,
                    userDTO,
                    blogPostEntity.getView(),
                    blogPostEntity.getCreatedDate());
//                  Date.from(Instant.ofEpochMilli((blogPostEntity.getCreatedDate().getTime())))
//                  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//                    .parse(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(blogPostEntity.getCreatedDate())));
            blogPostEntity.setView(blogPostEntity.getView() + 1);
            blogPostRepository.save(blogPostEntity);
            return blogPostDTO;
        } else
            throw new BlogPostException("not found blogpost with " + postId);

    }


    //xoá --> set Status = 0
    public void deleteBlogPost(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()
                && blogPostEntity.get().getStatus()) {
            BlogPostEntity blogPost = this.getBlogById(postId);

            blogPost.setStatus(false);

            blogPostRepository.save(blogPost);
        }
    }


    public BlogPostEntity updateBlogPost(RequestBlogPostDTO requestBlogPostDTO) {

        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(requestBlogPostDTO.getCategoryName(),
                requestBlogPostDTO.getParentCategoryId());
        Optional<UserEntity> userEntity = userRepository.findById(requestBlogPostDTO.getUserId());
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(requestBlogPostDTO.getPostId());

        if (blogPostEntity.isPresent()
                && categoryEntity.isPresent()
                && userEntity.isPresent()) {

            BlogPostEntity blogPost = this.getBlogById(requestBlogPostDTO.getPostId());

            CategoryEntity category = categoryEntity.get();
//            UserEntity authors = userEntity.get();
            Date modifiedDate = new Date();
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            modifiedDate.toInstant().atZone(zoneId).toLocalDateTime();
            Set<PostTagEntity> postTagEntities = new HashSet<>();

            for (TagDTO dto : requestBlogPostDTO.getTagList()) {
                PostTagEntity entity = new PostTagEntity();
                Optional<TagEntity> tagEntity = tagRepository.findById(dto.getTagId());
                if (!tagEntity.isPresent()) throw new PostTagException("tag not exist");
                entity.setId(dto.getTagId());
                postTagEntities.add(entity);
            }

            blogPost.setTypePost(requestBlogPostDTO.getTypePost());
            blogPost.setTitle(requestBlogPostDTO.getTitle());
            blogPost.setContent(requestBlogPostDTO.getContent());
            blogPost.setModifiedDate(modifiedDate);
            blogPost.setCategory(category);
            blogPost.setPostTags(postTagEntities);

            return blogPostRepository.save(blogPost);

        } else throw new BlogPostException("updated failed");

    }


    //sort theo thứ tự bài BlogPost mới nằm đầu tiên (giảm dần theo NGÀY)
    public void sort(List<BlogPostDTO> blogPostList) {

        Collections.sort(blogPostList, new Comparator<BlogPostDTO>() {

            @Override
            public int compare(BlogPostDTO o1, BlogPostDTO o2) {

                if (o1.getCreatedDate().compareTo(o2.getCreatedDate()) < 0) {
                    return 1;
                } else if (o1.getCreatedDate().compareTo(o2.getCreatedDate()) == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }

        });
    }

    public ResponseEntity<ResponseObject> getPinnedBlog() {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findByPinnedIsTrue();

        return !blogPostEntity.isEmpty() ? ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", blogPostEntity))

                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "not found", ""));
    }

    public ResponseEntity<ResponseObject> pinBlogAction(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findByPinnedIsTrue();
        Optional<BlogPostEntity> blogPost = blogPostRepository.findById(postId);

        if (blogPostEntity.isPresent()) {

            blogPostEntity.get().setPinned(false);

            blogPostRepository.save(blogPostEntity.get());

            if (blogPostEntity.get().getId().equals(postId)) {

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "unpinned successfull", ""));
            }
        }

        blogPost.get().setPinned(true);

        blogPostRepository.save(blogPost.get());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "pinned successfull", ""));
    }

    public List<BlogPostEntity> filterBlogPost(String filter, int page, int size) {
        List<BlogPostEntity> blogPostList = new ArrayList<>();
        Set<Long> existBlogPostId = new HashSet<>();

        while (blogPostList.size() < size) {
            Pageable pageable = PageRequest.of(page - 1, size - blogPostList.size());
            Page<BlogPostEntity> pageResult = null;
            if (filter.equalsIgnoreCase("")) {
                pageResult = blogPostRepository.findAll(pageable);
            } else if (!filter.trim().matches("\\d+")) {
                pageResult = blogPostRepository.getBlogPostEntitiesByTitle(filter, pageable);
            } else {
                Optional<CategoryEntity> categoryOptional = categoryRepository.findById(Long.parseLong(filter));
                pageResult = blogPostRepository.findBlogPostsByCategoryIdOrParentId(categoryOptional.get().getId(), pageable);
            }

            List<BlogPostEntity> pageContent = pageResult.getContent();

            for (BlogPostEntity blogPost : pageContent) {
                if (!existBlogPostId.contains(blogPost.getId())
                        && blogPost.getStatus()
                        && blogPost.getIsApproved()) {
                    blogPostList.add(blogPost);
                    existBlogPostId.add(blogPost.getId());
                }
            }

            if (!pageResult.hasNext()) {
                break;
            }
            page++;
        }
        return blogPostList;
    }


    public List<BlogPostEntity> getAllBlogPost(int page, int size) {
        return filterBlogPost("", page, size);
    }


    public List<BlogPostEntity> getAllBlogPostByTitle(String title, int page, int size) {
//        Pageable pageable = PageRequest.of(page - 1, size);
//        return blogPostRepository.getBlogPostEntitiesByTitle(title, pageable);
        return filterBlogPost(title, page, size);
    }

    //    public Page<BlogPostEntity> getBlogPostsByCategoryId(Long categoryId, int page, int size) {
    public List<BlogPostEntity> getBlogPostsByCategoryId(Long categoryId, int page, int size) {
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
//
//        if (!categoryOptional.isPresent()) {
//            return new PageImpl<>(Collections.emptyList());
//        }
//
//        CategoryEntity category = categoryOptional.get();
//        return blogPostRepository.findByCategory(category, pageable);
        return filterBlogPost(String.valueOf(categoryId), page, size);
    }

    public Page<BlogPostEntity> getSortedBlogPosts(String sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BlogPostEntity> blogPostEntities = null;

        if ("newest".equalsIgnoreCase(sortBy)) {
            blogPostEntities = blogPostRepository.findAllByOrderByCreatedDateDesc(pageable);
        } else if ("oldest".equalsIgnoreCase(sortBy)) {
            blogPostEntities = blogPostRepository.findAllByOrderByCreatedDateAsc(pageable);
        } else if ("latestModified".equalsIgnoreCase(sortBy)) {
            blogPostEntities = blogPostRepository.findAllByOrderByModifiedDateDesc(pageable);
        } else if ("oldestModified".equalsIgnoreCase(sortBy)) {
            blogPostEntities = blogPostRepository.findAllByOrderByModifiedDateAsc(pageable);
        } else if ("mostViewed".equalsIgnoreCase(sortBy)) {
            return blogPostRepository.findAllByOrderByViewDesc(pageable);
        } else {
            // Mặc định, sắp xếp theo ngày tạo mới nhất.
            blogPostEntities = blogPostRepository.findAllByOrderByCreatedDateDesc(pageable);
        }

        return blogPostEntities;
    }


    public BlogPostEntity insertBlogPost(RequestBlogPostDTO requestBlogPostDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(requestBlogPostDTO.getUserId());
        //check parentCategoryId != null
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(requestBlogPostDTO.getCategoryName(),
                requestBlogPostDTO.getParentCategoryId());
        if (userEntity.isPresent()
                && categoryEntity.isPresent()) {
            BlogPostEntity blogPostEntity = new BlogPostEntity
                    (requestBlogPostDTO.getTypePost(),
                            requestBlogPostDTO.getTitle(),
                            requestBlogPostDTO.getContent(),
                            categoryEntity.get(),
                            userEntity.get(),
                            0L,
                            false);
            userEntity.get().setPoint(userEntity.get().getPoint() + 1);
            return blogPostRepository.save(blogPostEntity);
        } else throw new BlogPostException("User or Category doesn't exists");
    }


    public Optional<CategoryEntity> findCategoryByNameAndParentId(String name, Long parentCategoryId) {
        CategoryEntity parentCategory = categoryRepository.findParentCategoryById(parentCategoryId);
        return categoryRepository.findByCategoryNameAndParentCategory(name, parentCategory);
    }



}
