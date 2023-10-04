package tech.fublog.FuBlog.service;


import tech.fublog.FuBlog.dto.request.RequestApprovalRequestDTO;
import tech.fublog.FuBlog.dto.response.ResponseApprovalRequestDTO;
import tech.fublog.FuBlog.entity.ApprovalRequestEntity;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.ApprovalRequestRepository;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.UserRepository;
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

    @Autowired
    public ApprovalRequestService(BlogPostRepository blogPostRepository, UserRepository userRepository, ApprovalRequestRepository approvalRequestRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
        this.approvalRequestRepository = approvalRequestRepository;
    }

    public List<ResponseApprovalRequestDTO> getAllApprovalRequest() {
        List<ApprovalRequestEntity> list = approvalRequestRepository.findAll();
        List<ResponseApprovalRequestDTO> dtoList = new ArrayList<>();
        for (ApprovalRequestEntity entity : list) {
            ResponseApprovalRequestDTO dto =
                    new ResponseApprovalRequestDTO(entity.getBlogPost().getId(),
                            entity.getRequest(),
                            entity.getReview());
            dtoList.add(dto);
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


    public ResponseEntity<ResponseObject> approveBlogPost(RequestApprovalRequestDTO requestApprovalRequestDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(requestApprovalRequestDTO.getReviewId());
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(requestApprovalRequestDTO.getPostId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            ApprovalRequestEntity approvalRequestEntity = approvalRequestRepository.findByBlogPost(blogPostEntity.get());
            if (approvalRequestEntity != null) {
                approvalRequestEntity.setReview(userEntity.get());
                approvalRequestEntity.setApproved(true);
                blogPostEntity.get().setApprovedBy(userEntity.get().getId());
                blogPostEntity.get().setIsApproved(true);
                blogPostRepository.save(blogPostEntity.get());
                approvalRequestRepository.save(approvalRequestEntity);
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