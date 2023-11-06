package tech.fublog.FuBlog.service;


import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.dto.request.ApprovalRequestRequestDTO;
import tech.fublog.FuBlog.dto.response.ApprovalRequestResponseDTO;
import tech.fublog.FuBlog.dto.response.PaginationResponseDTO;
import tech.fublog.FuBlog.entity.*;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApprovalRequestService {

    private final BlogPostRepository blogPostRepository;

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final ApprovalRequestRepository approvalRequestRepository;

    private final TagRepository tagRepository;

    private final PostTagRepository postTagRepository;

    @Autowired
    public ApprovalRequestService(BlogPostRepository blogPostRepository, UserRepository userRepository, CategoryRepository categoryRepository, ApprovalRequestRepository approvalRequestRepository, TagRepository tagRepository, PostTagRepository postTagRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.approvalRequestRepository = approvalRequestRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
    }

    public List<ApprovalRequestResponseDTO> getAllApprovalRequest() {
        List<ApprovalRequestEntity> list = approvalRequestRepository.findAllByApprovedIsFalse();
        List<ApprovalRequestResponseDTO> dtoList = new ArrayList<>();
        for (ApprovalRequestEntity entity : list) {
            if (!entity.isApproved()) {
                ApprovalRequestResponseDTO dto =
                        new ApprovalRequestResponseDTO(entity.getBlogPost().getId(), entity.getRequest().getId());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    public PaginationResponseDTO getAllApprovalRequestByCategory(Long categoryId) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
            List<ApprovalRequestEntity> approvalRequestEntityList = findRequestByCategory(categoryOptional.get().getName(),
                    categoryOptional.get().getParentCategory() == null ? null
                            : categoryOptional.get().getParentCategory().getId());

            List<ApprovalRequestResponseDTO> dtoList = new ArrayList<>();
            for (ApprovalRequestEntity entity : approvalRequestEntityList) {
                if (!entity.isApproved()) {
                    ApprovalRequestResponseDTO dto =
                            new ApprovalRequestResponseDTO(entity.getBlogPost().getId(), entity.getRequest().getId());
                    dtoList.add(dto);
                }
            }
        return new PaginationResponseDTO(dtoList, (long) dtoList.size(), 1L);
    }

    public List<ApprovalRequestEntity> findRequestByCategory(String name, Long parentCategoryId) {
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(name, parentCategoryId);
        if (categoryEntity.isPresent()) {
            List<CategoryEntity> categoryEntityList = findCategoryToSearch(categoryEntity.get(), new ArrayList<>());
            if (!categoryEntityList.isEmpty()) {
                List<ApprovalRequestEntity> approvalRequestEntity = new ArrayList<>();
                Set<ApprovalRequestEntity> approvalRequestEntitySet = new HashSet<>();
                List<ApprovalRequestEntity> approvalRequestEntityList = approvalRequestRepository.findByCategoryInAndIsApprovedFalse(categoryEntityList);
                if (approvalRequestEntityList != null) {
                    approvalRequestEntity.addAll(approvalRequestEntityList);
                }
                for (ApprovalRequestEntity entity : approvalRequestEntity) {
                    approvalRequestEntitySet.add(entity);
                }
                return approvalRequestEntityList;
            } else return new ArrayList<>();
        } else throw new BlogPostException("Category doesn't exists");
    }


    public ApprovalRequestEntity getApprovalRequestById(Long postId) {

        return approvalRequestRepository.findById(postId).orElse(null);
    }


    public ResponseEntity<ResponseObject> createApprovalRequestById(BlogPostEntity newBlogPost) {
        Optional<UserEntity> userEntity = userRepository.findById(newBlogPost.getAuthors().getId());

        ApprovalRequestEntity newApprovalRequest = new ApprovalRequestEntity(newBlogPost.getId(), false, userEntity.get(),
                null, newBlogPost);

        approvalRequestRepository.save(newApprovalRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "the post is waiting approve", ""));
    }


    public ResponseEntity<ResponseObject> approveBlogPost(ApprovalRequestRequestDTO approvalRequestRequestDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(approvalRequestRequestDTO.getReviewId());
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(approvalRequestRequestDTO.getPostId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            ApprovalRequestEntity approvalRequestEntity = approvalRequestRepository.findByBlogPost(blogPostEntity.get());
            if (approvalRequestEntity != null) {
                approvalRequestEntity.setReview(userEntity.get());
                approvalRequestEntity.setApproved(true);
                blogPostEntity.get().setApprovedBy(userEntity.get().getId());
                blogPostEntity.get().setIsApproved(true);
                blogPostRepository.save(blogPostEntity.get());
                approvalRequestRepository.save(approvalRequestEntity);
                for (TagDTO tag : approvalRequestRequestDTO.getTagList()) {
                    String tagName = tag.getTagName();
                    TagEntity tagEntity = tagRepository.findByTagName(tagName);
                    PostTagEntity postTagEntity = new PostTagEntity();
                    if (tagEntity != null) {
                        postTagEntity.setTag(tagEntity);
                        postTagEntity.setPost(blogPostEntity.get());
                        postTagRepository.save(postTagEntity);
                    } else {
                        TagEntity postTag = new TagEntity();
                        postTag.setTagName(tagName);
                        tagRepository.save(postTag);
                        TagEntity tagNameEntity = tagRepository.findByTagName(tagName);
                        postTagEntity.setTag(tagNameEntity);
                        postTagEntity.setPost(blogPostEntity.get());
                        postTagRepository.save(postTagEntity);
                    }
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "approved successful", ""));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "approved failed", ""));
    }

    public void insertApprovalRequest(BlogPostEntity blogPostEntity) {
        ApprovalRequestEntity approvalRequestEntity = new ApprovalRequestEntity(blogPostEntity);
        approvalRequestRepository.save(approvalRequestEntity);
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

}