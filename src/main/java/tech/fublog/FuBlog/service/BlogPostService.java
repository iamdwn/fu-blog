package tech.fublog.FuBlog.service;

import org.springframework.data.domain.*;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.request.RequestBlogPostDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.CategoryRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import tech.fublog.FuBlog.dto.response.ResponseBlogPostDTO;
import tech.fublog.FuBlog.dto.response.ResponseCommentDTO;
import tech.fublog.FuBlog.exception.BlogPostException;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    private BlogPostRepository blogPostRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

//    public ResponseEntity<ResponseObject> getAllBlogPosts() {
//        List<BlogPostEntity> blogPostEntity = blogPostRepository.findAll();
//        List<BlogPostDTO> blogPostList = new ArrayList<>();
//
//        if (!blogPostEntity.isEmpty()) {
//            for (int i = 0; i < blogPostEntity.size(); i++) {
//                if (blogPostEntity.get(i).getStatus() != null
//                        && blogPostEntity.get(i).getStatus()) {
//                    BlogPostDTO blogPost = new BlogPostDTO(blogPostEntity.get(i).getTypePost(),
//                            blogPostEntity.get(i).getTitle(),
//                            blogPostEntity.get(i).getContent(),
//                            blogPostEntity.get(i).getCategory().getId(),
//                            blogPostEntity.get(i).getAuthors().getId(),
//                            blogPostEntity.get(i).getCreatedDate(),
//                            blogPostEntity.get(i).getViews());
//
//                    blogPostList.add(blogPost);
//                }
//            }
//            sort(blogPostList);
//
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("ok", "found", blogPostList));
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ResponseObject("failed", "not found", ""));
//    }


    public BlogPostEntity getBlogById(Long postId) {

        return blogPostRepository.findById(postId).orElse(null);
    }

    public ResponseEntity<ResponseObject> getBlogPostById(Long postId) {

        BlogPostEntity blogPostEntity = blogPostRepository.findById(postId).orElse(null);

        if (blogPostEntity != null) {
            blogPostEntity.setView(blogPostEntity.getView());

            blogPostRepository.save(blogPostEntity);


            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("found", "post found", blogPostEntity));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("not found", "post not found", ""));
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

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "deleted failed", ""));
    }


//    public BlogPostEntity createBlogPost(BlogPostDTO blogPostDTO) {
//        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(blogPostDTO.getParentCategoryId());
//        Optional<UserEntity> userEntity = userRepository.findById(blogPostDTO.getUserId());
////        Optional<UserEntity> userEntity = userRepository.findByUsername(user);
//
//        if (categoryEntity.isPresent()
//                && userEntity.isPresent()) {
//
//
//            CategoryEntity category = categoryEntity.get();
//            UserEntity authors = userEntity.get();
//
//            Date createdDate = new Date();
//            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ Việt Nam
//            createdDate.toInstant().atZone(zoneId).toLocalDateTime();
//
//            String typePost = blogPostDTO.getTypePost();
//            String title = blogPostDTO.getTitle();
//            String content = blogPostDTO.getContent();
//            Double point = userEntity.get().getPoint();
//            userEntity.get().setPoint(point + 1);
//
////            //tạo và save bài BlogPost mới
//            BlogPostEntity newBlogPost = new BlogPostEntity(typePost, title, content,  null,
//                    null, true, false, category, authors, createdDate);
//            return blogPostRepository.save(newBlogPost);
//        }
//
//        return null;
//    }


    public ResponseEntity<ResponseObject> updateBlogPost(Long postId, BlogPostDTO blogPostDTO) {

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(blogPostDTO.getParentCategoryId());
        Optional<UserEntity> userEntity = userRepository.findById(blogPostDTO.getUser().getId());
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()
                && categoryEntity.isPresent()
                && userEntity.isPresent()) {

            BlogPostEntity blogPost = this.getBlogById(postId);

            CategoryEntity category = categoryEntity.get();
//            UserEntity authors = userEntity.get();
            Date modifiedDate = new Date();
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            modifiedDate.toInstant().atZone(zoneId).toLocalDateTime();

            blogPost.setTypePost(blogPostDTO.getTypePost());
            blogPost.setTitle(blogPostDTO.getTitle());
            blogPost.setContent(blogPostDTO.getContent());
            blogPost.setModifiedDate(modifiedDate);
            blogPost.setCategory(category);
//            blogPost.setAuthorsModified(authors);

            blogPostRepository.save(blogPost);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "updated successful", ""));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "updated failed", ""));
    }


//    public ResponseEntity<ResponseObject> findBlogByCategory(String name) {
//
//        Optional<CategoryEntity> categoryEntity = categoryRepository.findByCategoryName(name);
//        List<BlogPostDTO> blogPostList = new ArrayList<>();
//
//        if (categoryEntity != null) {
//
//            for (int i = 0; i < categoryEntity.get().getBlogPosts().size(); i++) {
//                if (categoryEntity.get().getBlogPosts().get(i).getStatus() != null
//                        && categoryEntity.get().getBlogPosts().get(i).getStatus()) {
//                    BlogPostDTO blogPost = new BlogPostDTO(categoryEntity.get().getBlogPosts().get(i).getTypePost(),
//                            categoryEntity.get().getBlogPosts().get(i).getTitle(),
//                            categoryEntity.get().getBlogPosts().get(i).getContent(),
//                            categoryEntity.get().getBlogPosts().get(i).getCategory().getId(),
//                            categoryEntity.get().getBlogPosts().get(i).getAuthors().getId(),
//                            categoryEntity.get().getBlogPosts().get(i).getCreatedDate(),
//                            categoryEntity.get().getBlogPosts().get(i).getViews());
//
//                    blogPostList.add(blogPost);
//                }
//            }
//
//
//            if (!blogPostList.isEmpty()) {
//
//                return ResponseEntity.status(HttpStatus.OK)
//                        .body(new ResponseObject("ok", "found", blogPostList));
//            } else {
//
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ResponseObject("failed", "cannot found any blog with category", ""));
//            }
//        } else {
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseObject("failed", "category not exists", ""));
//        }
//    }


    //sort theo thứ tự bài BlogPost mới nằm đầu tiên (giảm dần theo NGÀY)
    public void sort(List<BlogPostDTO> blogPostList) {

        Collections.sort(blogPostList, new Comparator<BlogPostDTO>() {

            @Override
            public int compare(BlogPostDTO o1, BlogPostDTO o2) {

                if (o1.getCreateDate().compareTo(o2.getCreateDate()) < 0) {
                    return 1;
                } else if (o1.getCreateDate().compareTo(o2.getCreateDate()) == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }

        });
    }

    public List<BlogPostEntity> getAllBlogPost(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return blogPostRepository.findAll(pageable).get().toList();
    }

    public List<BlogPostEntity> getAllBlogPostByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return blogPostRepository.getBlogPostEntitiesByTitle(title, pageable);
    }

    public Page<BlogPostEntity> getBlogPostsByCategoryId(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);

        if (!categoryOptional.isPresent()) {
            return new PageImpl<>(Collections.emptyList());
        }

        CategoryEntity category = categoryOptional.get();
        return blogPostRepository.findByCategory(category, pageable);
    }

    public Page<BlogPostEntity> getSortedBlogPosts(String sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BlogPostEntity> blogPostEntities = null;

        if ("newest".equalsIgnoreCase(sortBy)) {
            blogPostEntities = blogPostRepository.findAllByOrderByCreatedDateDesc(pageable);
        } else if ("oldest".equalsIgnoreCase(sortBy)) {
            blogPostEntities = blogPostRepository.findAllByOrderByCreatedDateAsc(pageable);
        } else if ("latestModified".equalsIgnoreCase(sortBy)) {
//            blogPostEntities = blogPostRepository.findAllByOrderByModifiedDateDesc(pageable);
        } else if ("oldestModified".equalsIgnoreCase(sortBy)) {
//            blogPostEntities = blogPostRepository.findAllByOrderByModifiedDateAsc(pageable);
        } else if ("mostViewed".equalsIgnoreCase(sortBy)) {
//            return blogPostRepository.findAllByOrderByViewsDesc(pageable);
        } else {
            // Mặc định, sắp xếp theo ngày tạo mới nhất.
            blogPostEntities = blogPostRepository.findAllByOrderByCreatedDateDesc(pageable);
        }

        return blogPostEntities;
    }

    public List<BlogPostEntity> getPageOfItems(List<BlogPostEntity> items, int page, int pageSize) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, items.size());

        if (startIndex >= items.size()) {
            return Collections.emptyList(); // Trang không có phần tử nào
        }

        return items.subList(startIndex, endIndex);
    }


    public Set<BlogPostDTO> findBlogByCategory(String name, Long parentCategoryId, int page, int size) {
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(name, parentCategoryId);
        if (categoryEntity.isPresent()) {
            List<CategoryEntity> categoryEntityList = findCategoryToSearch(categoryEntity.get(), new ArrayList<>());
            if (!categoryEntityList.isEmpty()) {
                List<BlogPostEntity> blogPostEntitySet = new ArrayList<>();
                Set<BlogPostDTO> blogPostDTOSet = new HashSet<>();
                Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
                Page<BlogPostEntity> blogPostEntityPage = blogPostRepository.findByCategoryInAndIsApprovedTrueAndStatusTrue(categoryEntityList, pageable);
                if (blogPostEntityPage != null) {
                    blogPostEntitySet.addAll(blogPostEntityPage.getContent());
                }
                for (BlogPostEntity entity : blogPostEntitySet) {
                    blogPostDTOSet.add(convertPostToDTO(entity));
                }
                return blogPostDTOSet;
            } else return new HashSet<>();
        } else throw new BlogPostException("Category doesn't exists");
    }

    public List<BlogPostDTO> findBlogByTitle(String title) {
        if (!title.trim().isEmpty()) {
            List<BlogPostEntity> list = blogPostRepository.findByTitleLikeAndIsApprovedTrueAndStatusTrue("%" + title.trim() + "%");
            if (!list.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity entity : list) {
//                    if (entity.getIsApproved() && entity.getStatus()) {
                    dtoList.add(convertPostToDTO(entity));
//                    }
                }
                return dtoList;
            } else return new ArrayList<>();
        } else throw new BlogPostException("Nothing to search");
    }

    public BlogPostEntity insertBlogPost(RequestBlogPostDTO requestBlogPostDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(requestBlogPostDTO.getUserId());
        //check parentCategoryId != null
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(requestBlogPostDTO.getCategoryName(), requestBlogPostDTO.getParentCategoryId());
        if (userEntity.isPresent() && categoryEntity.isPresent()) {
            BlogPostEntity blogPostEntity = new BlogPostEntity
                    (requestBlogPostDTO.getTypePost(), requestBlogPostDTO.getTitle(), requestBlogPostDTO.getContent(), categoryEntity.get(), userEntity.get());
            return blogPostRepository.save(blogPostEntity);
        } else throw new BlogPostException("User or Category doesn't exists");
    }

    public ResponseBlogPostDTO viewBlogPost(Long postId, Long vote, List<ResponseCommentDTO> comment) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            return convertResponseDTO(blogPostEntity.get(), vote, comment);
        } else throw new BlogPostException("Blog doesn't exists");
    }

    public UserDTO convertUserToDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getFullName(), userEntity.getPassword(), userEntity.getEmail(), userEntity.getId(), userEntity.getPicture(), new ArrayList<>());
    }

    public BlogPostDTO convertPostToDTO(BlogPostEntity blogPostEntity) {
        Long parentCategoryId = null;
        if (blogPostEntity.getCategory().getParentCategory() != null)
            parentCategoryId = blogPostEntity.getCategory().getParentCategory().getId();
        return new BlogPostDTO(blogPostEntity.getId(), blogPostEntity.getCreatedDate(), blogPostEntity.getTypePost(),
                blogPostEntity.getTitle(), blogPostEntity.getContent(),
                blogPostEntity.getCategory().getCategoryName(),
                parentCategoryId, null, convertUserToDTO(blogPostEntity.getAuthors()));
    }

    public ResponseBlogPostDTO convertResponseDTO(BlogPostEntity blogPostEntity, Long vote, List<ResponseCommentDTO> comment) {
        Long parentCategoryId = null;
        if (blogPostEntity.getCategory().getParentCategory() != null)
            parentCategoryId = blogPostEntity.getCategory().getParentCategory().getId();
        return new ResponseBlogPostDTO(blogPostEntity.getId(), blogPostEntity.getAuthors().getId(),
                blogPostEntity.getTypePost(), blogPostEntity.getTitle(),
                blogPostEntity.getContent(), blogPostEntity.getCategory().getCategoryName(),
                parentCategoryId, vote, comment);
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
