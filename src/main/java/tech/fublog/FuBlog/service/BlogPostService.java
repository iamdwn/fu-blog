package tech.fublog.FuBlog.service;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.fublog.FuBlog.Utility.DTOConverter;
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

            UserInfoResponseDTO userDTO = new UserInfoResponseDTO(
                    userEntity.getId(),
                    userEntity.getFullName(),
                    userEntity.getPicture(),
                    userEntity.getEmail(),
                    roleNames.get(roleNames.size() - 1),
                    roleNames,
                    userEntity.getPoint()
            );

            blogPostEntity.setView(blogPostEntity.getView() + 1);
            blogPostRepository.save(blogPostEntity);

            BlogPostDTO blogPostDTO = new BlogPostDTO(blogPostEntity.getId(),
                    blogPostEntity.getTypePost(),
                    blogPostEntity.getTitle(),
                    blogPostEntity.getContent(),
                    blogPostEntity.getImage(),
                    blogPostEntity.getCategory().getName(),
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

            blogPostEntity.get().setStatus(false);

            blogPostRepository.save(blogPostEntity.get());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "deleted successful", ""));
        }
        throw new BlogPostException("this blog was deleted or the blog not exist");
    }


    public void deleteBlogPostByUser(UserEntity user) {
        List<BlogPostEntity> blogPostEntity = blogPostRepository.findByAuthorsAndStatusIsTrue(user);

        if (!blogPostEntity.isEmpty()) {
            for (BlogPostEntity entity : blogPostEntity) {
                entity.setStatus(false);
                blogPostRepository.save(entity);
            }
        }

    }


    public BlogPostEntity updateBlogPost(BlogPostRequestDTO blogPostRequestDTO) {

        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(blogPostRequestDTO.getCategoryName(),
                blogPostRequestDTO.getParentCategoryId());
        Optional<UserEntity> userEntity = userRepository.findById(blogPostRequestDTO.getUserId());
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(blogPostRequestDTO.getPostId());

        if (blogPostEntity.isPresent()
                && categoryEntity.isPresent()
                && userEntity.get().getStatus()) {

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

    public PaginationResponseDTO getBlogPostByAuthor(Long userId, int pageNumber, int size) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        Page<BlogPostEntity> pageResult;
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        if (userEntity.isPresent()) {
            pageResult = blogPostRepository.findByAuthorsAndStatusTrueAndIsApprovedTrueOrderByCreatedDateDesc(userEntity.get(), pageable);
            if (!pageResult.isEmpty()) {
                List<BlogPostEntity> pageContent = pageResult.getContent();
                blogPostDTOList = new ArrayList<>();
                for (BlogPostEntity entity : pageContent) {
                    blogPostDTOList.add(DTOConverter.convertPostToDTO(entity.getId()));
                }
            }
        } else throw new BlogPostException("User doesn't exists");
        Long blogPostCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();
        return new PaginationResponseDTO(blogPostDTOList, blogPostCount, pageCount);
    }

//    public List<BlogPostDTO> getBlogPostByTag(Long tagId) {
//        Optional<TagEntity> tagEntity = tagRepository.findById(tagId);
//        if (tagEntity.isPresent()) {
//            List<PostTagEntity> postTagEntityList = postTagRepository.findByTag(tagEntity.get());
//            if (!postTagEntityList.isEmpty()) {
//                List<BlogPostDTO> dtoList = new ArrayList<>();
//                for (PostTagEntity postTagEntity : postTagEntityList) {
//                    dtoList.add(convertBlogPostDTO(postTagEntity.getPost()));
//                }
//                return dtoList;
//            } else return new ArrayList<>();
//        } else throw new BlogPostException("Tag doesn't exists");
//    }

    public void sort(List<BlogPostDTO> blogPostList) {

        Collections.sort(blogPostList, new Comparator<BlogPostDTO>() {
            @Override
            public int compare(BlogPostDTO o1, BlogPostDTO o2) {
                double point1 = o1.getVoteCount() * 0.4 + o1.getViews() * 0.6;
                double point2 = o2.getVoteCount() * 0.4 + o2.getViews() * 0.6;
                if (point1 < point2) {
                    return 1;
                } else if (point1 == point2) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    public ResponseEntity<ResponseObject> getPinnedBlog() {
        BlogPostEntity blogPostEntity = blogPostRepository.findByPinnedIsTrue();

        return blogPostEntity != null ? ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", DTOConverter.convertPostToDTO(blogPostEntity.getId())))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "not found", ""));
    }

    public ResponseEntity<ResponseObject> pinBlogAction(Long postId) {
        BlogPostEntity blogPostEntity = blogPostRepository.findByPinnedIsTrue();
        Optional<BlogPostEntity> blogPost = blogPostRepository.findById(postId);

        if (blogPostEntity != null) {

            blogPostEntity.setPinned(false);
            blogPostRepository.save(blogPostEntity);
            if (blogPostEntity.getId().equals(postId)) {

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "unpinned successfull", ""));
            }
        }
        blogPost.get().setPinned(true);
        blogPostRepository.save(blogPost.get());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "pinned successfull", ""));
    }

    public PaginationResponseDTO filterBlogPost(String filter, int pageNumber, int size) {
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        List<BlogPostEntity> pageContent;
        Page<BlogPostEntity> pageResult;

        Pageable pageable = PageRequest.of(pageNumber - 1, size);


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
            pageable = PageRequest.of(pageNumber - 1, size, sort);
            filter = "%" + filter + "%";
            pageResult = blogPostRepository.getBlogPostEntitiesByTitleLikeAndIsApprovedIsTrueAndStatusIsTrue(filter, pageable);
        } else {
            Long cateroryId = Long.parseLong(filter);
            Optional<CategoryEntity> categoryOptional = categoryRepository.findById(cateroryId);
//            pageResult = blogPostRepository.findBlogPostsByCategoryIdOrParentId(categoryOptional.get().getId(), pageable);

            pageResult = findBlogByCategory(categoryOptional.get().getName(),
                    categoryOptional.get().getParentCategory() == null ? null
                            : categoryOptional.get().getParentCategory().getId(), pageNumber, size);
        }

        if (pageResult != null) {
            pageContent = pageResult.getContent();
            for (BlogPostEntity blogPost : pageContent) {
                blogPostDTOList.add(DTOConverter.convertPostToDTO(blogPost.getId()));
            }
        }

        Long blogPostCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();

        return new PaginationResponseDTO(blogPostDTOList, blogPostCount, pageCount);
    }


    public Page<BlogPostEntity> findBlogByCategory(String name, Long parentCategoryId, int page, int size) {
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(name, parentCategoryId);
        if (categoryEntity.isPresent()) {
            List<CategoryEntity> categoryEntityList = findCategoryToSearch(categoryEntity.get(), new ArrayList<>());
            if (!categoryEntityList.isEmpty()) {
                List<BlogPostEntity> blogPostEntitySet = new ArrayList<>();
                Set<BlogPostEntity> blogPostSet = new HashSet<>();
                Pageable pageable = PageRequest.of(page - 1, size);
                Page<BlogPostEntity> blogPostEntityPage = blogPostRepository.findByCategoryInAndIsApprovedTrueAndStatusTrue(categoryEntityList, pageable);
                if (blogPostEntityPage != null) {
                    blogPostEntitySet.addAll(blogPostEntityPage.getContent());
                }
                for (BlogPostEntity entity : blogPostEntitySet) {
                    blogPostSet.add(entity);
                }
                return blogPostEntityPage;
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

    public List<BlogPostDTO> getPopularBlogPostByView() {
        List<BlogPostDTO> popularBlogPostList = new ArrayList<>();
        List<BlogPostEntity> blogPostEntity = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByViewDesc();
        if (blogPostEntity != null) {
            for (BlogPostEntity entity : blogPostEntity) {
                popularBlogPostList.add(DTOConverter.convertPostToDTO(entity.getId()));
            }
            return popularBlogPostList;
        }
        throw new BlogPostException("not found");
    }

    public List<BlogPostDTO> getPopularBlogPostByVote() {
        List<BlogPostDTO> popularBlogPostList = new ArrayList<>();
        List<BlogPostEntity> blogPostEntity = blogPostRepository.findAllByStatusTrueAndIsApprovedTrueOrderByVoteDesc();
        if (blogPostEntity != null) {
            for (BlogPostEntity entity : blogPostEntity) {
                popularBlogPostList.add(DTOConverter.convertPostToDTO(entity.getId()));
            }
            return popularBlogPostList;
        }
        throw new BlogPostException("not found");
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
                && userEntity.get().getStatus()
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
        return categoryRepository.findByNameAndParentCategory(name, parentCategory);
    }

    public PaginationResponseDTO findByTagName(String tagName, int pageNumber, int size) {
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        List<BlogPostEntity> pageContent;
        Page<BlogPostEntity> pageResult = null;

        Pageable pageable = PageRequest.of(pageNumber - 1, size);

        TagEntity tag = tagRepository.findByTagName(tagName);
        if (tag != null) {
            pageResult = blogPostRepository.findByPostTagsTag(tag, pageable);
        } else throw new BlogPostException("Tag doesn't exists");

        if (pageResult != null) {
            pageContent = pageResult.getContent();
            for (BlogPostEntity blogPost : pageContent) {
                blogPostDTOList.add(DTOConverter.convertPostToDTO(blogPost.getId()));
            }
        }
        Long blogPostCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();

        return new PaginationResponseDTO(blogPostDTOList, blogPostCount, pageCount);
    }

    public PaginationResponseDTO getBlogByTrending(int page, int size) {
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BlogPostEntity> pageResult = blogPostRepository.findAllByTrending(pageable);
        if (pageResult != null) {
//            List<BlogPostEntity> sortedList = pageResult
//                    .stream()
//                    .sorted(Comparator.comparing(BlogPostEntity::getView)
//                            .thenComparing(BlogPostEntity::getCreatedDate).reversed())
//                    .collect(Collectors.toList());
            for (BlogPostEntity blogPost : pageResult.getContent()) {
                blogPostDTOList.add(DTOConverter.convertPostToDTO(blogPost.getId()));
            }
        }

        Long blogPostCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();

        return new PaginationResponseDTO(blogPostDTOList, blogPostCount, pageCount);
    }

    public PaginationResponseDTO getBlogByVote(int page, int size) {
        List<BlogPostDTO> blogPostDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BlogPostEntity> pageResult = blogPostRepository.findAllByVote(pageable);
        if (pageResult != null) {
//            List<BlogPostEntity> sortedList = pageResult
//                    .stream()
//                    .sorted((post1, post2) -> {
//                        int voteCount1 = post1.getVotes().size();
//                        int voteCount2 = post2.getVotes().size();
//                        if (voteCount1 == voteCount2)
//                            return post2.getCreatedDate().compareTo(post1.getCreatedDate());
//                        return voteCount2 - voteCount1;
//                    })
//                    .collect(Collectors.toList());
            for (BlogPostEntity blogPost : pageResult.getContent()) {
                blogPostDTOList.add(DTOConverter.convertPostToDTO(blogPost.getId()));
            }
        }

        Long blogPostCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();

        return new PaginationResponseDTO(blogPostDTOList, blogPostCount, pageCount);
    }

}
