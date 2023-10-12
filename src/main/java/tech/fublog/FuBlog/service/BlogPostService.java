package tech.fublog.FuBlog.service;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.request.BlogPostRequestDTO;
import tech.fublog.FuBlog.dto.response.PaginationResponseDTO;
import tech.fublog.FuBlog.dto.response.UserInfoResponseDTO;
import tech.fublog.FuBlog.entity.*;
import tech.fublog.FuBlog.exception.PostTagException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tech.fublog.FuBlog.exception.BlogPostException;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final TagRepository tagRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final PostTagRepository postTagRepository;

    @Autowired
    public BlogPostService(CategoryRepository categoryRepository, UserRepository userRepository, BlogPostRepository blogPostRepository, TagRepository tagRepository, VoteRepository voteRepository, CommentRepository commentRepository,
                           PostTagRepository postTagRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
        this.tagRepository = tagRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.postTagRepository = postTagRepository;
    }


    public BlogPostEntity getBlogById(Long postId) {

        return blogPostRepository.findById(postId).orElse(null);
    }

    public BlogPostDTO getBlogPostById(Long postId) {

        BlogPostEntity blogPostEntity = blogPostRepository.findById(postId).orElse(null);

        if (blogPostEntity != null) {
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
                    /*userEntity.getPassword()*/null,
                    userEntity.getEmail(),
                    userEntity.getId(),
                    userEntity.getPicture(),
                    userEntity.getStatus(),
                    roleNames.get(roleNames.size() - 1),
                    roleNames);

            blogPostEntity.setView(blogPostEntity.getView() + 1);
            blogPostRepository.save(blogPostEntity);

            BlogPostDTO blogPostDTO = new BlogPostDTO(blogPostEntity.getId(),
                    blogPostEntity.getTypePost(),
                    blogPostEntity.getTitle(),
                    blogPostEntity.getContent(),
                    blogPostEntity.getImage(),
                    blogPostEntity.getCategory().getCategoryName(),
                    blogPostEntity.getCategory().getParentCategory(),
                    tagDTOs,
                    userDTO,
                    blogPostEntity.getView(),
                    blogPostEntity.getCreatedDate(),
                    voteRepository.countByPostVote(blogPostEntity),
                    commentRepository.countByPostComment(blogPostEntity)
            );
//                  Date.from(Instant.ofEpochMilli((blogPostEntity.getCreatedDate().getTime())))
//                  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//                    .parse(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(blogPostEntity.getCreatedDate())));

            return blogPostDTO;
        } else
            throw new BlogPostException("not found blogpost with " + postId);

    }

    public BlogPostDTO convertBlogPostDTO(BlogPostEntity blogPostEntity) {

//        BlogPostEntity blogPostEntity = blogPostRepository.findById(postId).orElse(null);

        if (blogPostEntity != null) {
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
                    /*userEntity.getPassword()*/null,
                    userEntity.getEmail(),
                    userEntity.getId(),
                    userEntity.getPicture(),
                    userEntity.getStatus(),
                    roleNames.get(roleNames.size() - 1),
                    roleNames);

//            UserInfoResponseDTO userDTO = new UserInfoResponseDTO(
//                    userEntity.getId(),
//                    userEntity.getFullName(),
//                    userEntity.getPicture(),
//                    userEntity.getEmail(),
//                    roleNames.get(roleNames.size() - 1),
//                    roleNames,
//                    userEntity.getPoint()
//            );

            BlogPostDTO blogPostDTO = new BlogPostDTO(blogPostEntity.getId(),
                    blogPostEntity.getTypePost(),
                    blogPostEntity.getTitle(),
                    blogPostEntity.getContent(),
                    blogPostEntity.getImage(),
                    blogPostEntity.getCategory().getCategoryName(),
                    blogPostEntity.getCategory().getParentCategory(),
                    tagDTOs,
                    userDTO,
                    blogPostEntity.getView(),
                    blogPostEntity.getCreatedDate(),
                    voteRepository.countByPostVote(blogPostEntity),
                    commentRepository.countByPostComment(blogPostEntity)
            );
//                  Date.from(Instant.ofEpochMilli((blogPostEntity.getCreatedDate().getTime())))
//                  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//                    .parse(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(blogPostEntity.getCreatedDate())));

            return blogPostDTO;
        } else
            throw new BlogPostException("not found blogpost");

    }

    public BlogPostEntity getBlogPostDetailsById(Long postId) {
        BlogPostEntity blogPostEntity = blogPostRepository.findById(postId).orElse(null);

        if (blogPostEntity != null) {
            UserEntity userEntity = userRepository.findById(blogPostEntity.getAuthors().getId()).orElse(null);
            blogPostEntity.setView(blogPostEntity.getView() + 1);
            return blogPostRepository.save(blogPostEntity);

        } else
            throw new BlogPostException("not found blogpost with " + postId);
    }


    //xoá --> set Status = 0
    public ResponseEntity<ResponseObject> deleteBlogPost(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);

        if (blogPostEntity.isPresent()
                && blogPostEntity.get().getStatus()) {
            BlogPostEntity blogPost = this.getBlogById(postId);

            blogPost.setStatus(false);

            blogPostRepository.save(blogPost);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "deleted successful", ""));
        }
        throw new BlogPostException("this blog was deleted or the blog not exist");
    }


    public BlogPostEntity updateBlogPost(BlogPostRequestDTO blogPostRequestDTO) {
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(blogPostRequestDTO.getCategoryName(),
                blogPostRequestDTO.getParentCategoryId());
        Optional<UserEntity> userEntity = userRepository.findById(blogPostRequestDTO.getUserId());
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(blogPostRequestDTO.getPostId());
        if (blogPostEntity.isPresent()
                && categoryEntity.isPresent()
                && userEntity.isPresent()) {
            BlogPostEntity blogPost = this.getBlogById(blogPostRequestDTO.getPostId());
            CategoryEntity category = categoryEntity.get();
//            UserEntity authors = userEntity.get();
            Date modifiedDate = new Date();
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            modifiedDate.toInstant().atZone(zoneId).toLocalDateTime();
            Set<PostTagEntity> postTagEntities = new HashSet<>();
            for (TagDTO dto : blogPostRequestDTO.getTagList()) {
                PostTagEntity entity = new PostTagEntity();
                Optional<TagEntity> tagEntity = tagRepository.findById(dto.getTagId());
                if (!tagEntity.isPresent()) throw new PostTagException("tag not exist");
                entity.setId(dto.getTagId());
                postTagEntities.add(entity);
            }
            blogPost.setTypePost(blogPostRequestDTO.getTypePost());
            blogPost.setTitle(blogPostRequestDTO.getTitle());
            blogPost.setContent(blogPostRequestDTO.getContent());
            blogPost.setModifiedDate(modifiedDate);
            blogPost.setCategory(category);
            blogPost.setPostTags(postTagEntities);
            return blogPostRepository.save(blogPost);
        } else throw new BlogPostException("updated failed");

    }

    public List<BlogPostDTO> getBlogPostByAuthor(Long userId, int page, int size) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").ascending());
            Page<BlogPostEntity> entityList = blogPostRepository.findByAuthorsAndStatusTrueAndIsApprovedTrue(userEntity.get(), pageable);
            if (!entityList.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity entity : entityList.getContent()) {
                    dtoList.add(convertBlogPostDTO(entity));
                }
                return dtoList;
            } else return new ArrayList<>();
        } else throw new BlogPostException("User doesn't exists");
    }

    public List<BlogPostDTO> getBlogPostByTag(Long tagId, int page, int size) {
        Optional<TagEntity> tagEntity = tagRepository.findById(tagId);
        if (tagEntity.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<PostTagEntity> postTagEntityList = postTagRepository.findByTag(tagEntity.get(), pageable);
            if (!postTagEntityList.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (PostTagEntity postTagEntity : postTagEntityList.getContent()) {
                    dtoList.add(convertBlogPostDTO(postTagEntity.getPost()));
                }
                return dtoList;
            } else return new ArrayList<>();
        } else throw new BlogPostException("Tag doesn't exists");
    }

    public void sortByVote(List<BlogPostDTO> blogPostList) {
//        mergeSortByView(blogPostList);
        blogPostList.sort(new Comparator<BlogPostDTO>() {
            @Override
            public int compare(BlogPostDTO post1, BlogPostDTO post2) {
                return Long.compare(post2.getVoteCount(), post1.getVoteCount());
            }
        });
    }

    public void sortByView(List<BlogPostDTO> blogPostList) {
//        mergeSortByView(blogPostList);
        blogPostList.sort(new Comparator<BlogPostDTO>() {
            @Override
            public int compare(BlogPostDTO post1, BlogPostDTO post2) {
                return Long.compare(post2.getViews(), post1.getViews());
            }
        });
    }

    public ResponseEntity<ResponseObject> getPinnedBlog() {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findByPinnedIsTrue();
        BlogPostDTO blogPostDTO = convertBlogPostDTO(blogPostEntity.get());

        return !blogPostEntity.isEmpty() ? ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", blogPostDTO))
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

    public PaginationResponseDTO filterBlogPost(String filter, int page, int size) {
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        List<BlogPostEntity> pageContent;
        Page<BlogPostEntity> pageResult = null;

        Pageable pageable = PageRequest.of(page - 1, size);

        if (filter.equalsIgnoreCase("")) {
            pageResult = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByCreatedDateDesc(pageable);
        } else if ("newest".equalsIgnoreCase(filter)) {
            pageResult = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByCreatedDateDesc(pageable);
        } else if ("oldest".equalsIgnoreCase(filter)) {
            pageResult = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByCreatedDateAsc(pageable);
        } else if ("latestModified".equalsIgnoreCase(filter)) {
            pageResult = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByModifiedDateDesc(pageable);
        } else if ("oldestModified".equalsIgnoreCase(filter)) {
            pageResult = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByModifiedDateAsc(pageable);
        } else if ("mostViewed".equalsIgnoreCase(filter)) {
            pageResult = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByViewDesc(pageable);
        } else if (!filter.trim().matches("\\d+")) {
            Sort sort = Sort.by(Sort.Direction.ASC, "title");
            pageable = PageRequest.of(page - 1, size, sort);
            filter = "%" + filter + "%";
            pageResult = blogPostRepository.getBlogPostEntitiesByTitleLikeAndIsApprovedIsTrueAndStatusIsTrue(filter, pageable);
        } else {
            Long categoryId = Long.parseLong(filter);
            Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
//            pageResult = blogPostRepository.findBlogPostsByCategoryIdOrParentId(categoryOptional.get().getId(), pageable);
            if (categoryOptional.isPresent())
                pageResult = findBlogByCategory(categoryOptional.get().getCategoryName(),
                        categoryOptional.get().getParentCategory() == null ? null
                                : categoryOptional.get().getParentCategory().getId(), page, size);
        }

        if (pageResult != null) {
            pageContent = pageResult.getContent();
            for (BlogPostEntity blogPost : pageContent) {
                blogPostDTOList.add(convertBlogPostDTO(blogPost));
            }
        }

        Long blogPostCount = Long.valueOf(pageResult.getTotalElements());
        Long pageCount = Long.valueOf(pageResult.getTotalPages());

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO(blogPostDTOList, blogPostCount, pageCount);
        return paginationResponseDTO;
    }


    public Page<BlogPostEntity> findBlogByCategory(String name, Long parentCategoryId, int page, int size) {
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(name, parentCategoryId);
        if (categoryEntity.isPresent()) {
            List<CategoryEntity> categoryEntityList = findCategoryToSearch(categoryEntity.get(), new ArrayList<>());
            if (!categoryEntityList.isEmpty()) {
                Pageable pageable = PageRequest.of(page - 1, size);
                return blogPostRepository.findByCategoryInAndIsApprovedTrueAndStatusTrue(categoryEntityList, pageable);
            } else return new PageImpl<>(Collections.emptyList());
        } else throw new BlogPostException("Category doesn't exists");
    }

//    public static Page<BlogPostEntity> filterDuplicateEntities(Page<BlogPostEntity> page) {
//        List<BlogPostEntity> content = page.getContent();
//        Set<Long> seenIds = new HashSet<>();
//        List<BlogPostEntity> uniqueEntities = content.stream()
//                .filter(entity -> seenIds.add(entity.getId()))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(uniqueEntities, page.getPageable(), uniqueEntities.size());
//    }

    public List<BlogPostDTO> getPopularBlogPostByVote() {
        List<BlogPostDTO> popularBlogPostList = new ArrayList<>();
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        List<BlogPostEntity> blogPostEntity = blogPostRepository.findAll();

        for (BlogPostEntity entity : blogPostEntity) {
            blogPostDTOList.add(convertBlogPostDTO(entity));
        }
        sortByVote(blogPostDTOList);
        for (BlogPostDTO entity : blogPostDTOList) {
            if (popularBlogPostList.size() <= 6) {
                popularBlogPostList.add(entity);
            } else return popularBlogPostList;
        }
        throw new BlogPostException("not found");
    }

    public List<BlogPostDTO> getPopularBlogPostByView() {
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 6);
        Page<BlogPostEntity> blogPostEntity = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByViewDesc(pageable);

        for (BlogPostEntity entity : blogPostEntity.getContent()) {
            blogPostDTOList.add(convertBlogPostDTO(entity));
        }
        return blogPostDTOList;
    }

    public PaginationResponseDTO getAllBlogPost(int page, int size) {
        return filterBlogPost("", page, size);
    }


    public PaginationResponseDTO getAllBlogPostByTitle(String title, int page, int size) {
        return filterBlogPost(title, page, size);
    }

    public PaginationResponseDTO getBlogPostsByCategoryId(Long categoryId, int page, int size) {
        return filterBlogPost(String.valueOf(categoryId), page, size);
    }

    public PaginationResponseDTO getSortedBlogPosts(String sortBy, int page, int size) {
        return filterBlogPost(sortBy, page, size);
    }


    public BlogPostEntity insertBlogPost(BlogPostRequestDTO blogPostRequestDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(blogPostRequestDTO.getUserId());
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(blogPostRequestDTO.getCategoryName(),
                blogPostRequestDTO.getParentCategoryId());
        if (userEntity.isPresent()
                && categoryEntity.isPresent()) {
            BlogPostEntity blogPostEntity = new BlogPostEntity
                    (blogPostRequestDTO.getTypePost(),
                            blogPostRequestDTO.getTitle(),
                            blogPostRequestDTO.getContent(),
                            blogPostRequestDTO.getImage(),
                            categoryEntity.get(),
                            userEntity.get(),
                            0L,
                            false);
            userEntity.get().setPoint(userEntity.get().getPoint() + 1);
            return blogPostRepository.save(blogPostEntity);
        } else throw new BlogPostException("User or Category doesn't exists");
    }

    public long countPostMarkByUser(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            return userEntity.get().getMarkPosts().size();
        }
        return 0L;
    }

    public List<CategoryEntity> findCategoryToSearch(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntityList) {
        List<CategoryEntity> subEntityList = categoryRepository.findByParentCategory(categoryEntity);
        if (subEntityList.isEmpty()) {
            categoryEntityList.add(categoryEntity);
        } else {
            categoryEntityList.add(categoryEntity);
            for (CategoryEntity entity : subEntityList) {
                findCategoryToSearch(entity, categoryEntityList);
            }
        }
        return categoryEntityList;
    }

    public Optional<CategoryEntity> findCategoryByNameAndParentId(String name, Long parentCategoryId) {
        CategoryEntity parentCategory = categoryRepository.findParentCategoryById(parentCategoryId);
        return categoryRepository.findByCategoryNameAndParentCategory(name, parentCategory);
    }

}
