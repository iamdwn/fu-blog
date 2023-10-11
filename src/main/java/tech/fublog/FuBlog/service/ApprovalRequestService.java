package tech.fublog.FuBlog.service;


import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.dto.request.ApprovalRequestRequestDTO;
import tech.fublog.FuBlog.dto.response.ApprovalRequestResponseDTO;
import tech.fublog.FuBlog.entity.*;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalRequestService {

    private final BlogPostRepository blogPostRepository;

    private final UserRepository userRepository;

    private final ApprovalRequestRepository approvalRequestRepository;

    private final TagRepository tagRepository;

    private final PostTagRepository postTagRepository;

    @Autowired
    public ApprovalRequestService(BlogPostRepository blogPostRepository, UserRepository userRepository, ApprovalRequestRepository approvalRequestRepository, TagRepository tagRepository, PostTagRepository postTagRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
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
}