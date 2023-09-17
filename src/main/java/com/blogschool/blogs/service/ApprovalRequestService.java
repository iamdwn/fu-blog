package com.blogschool.blogs.service;


import com.blogschool.blogs.dto.ApprovalRequestDTO;
import com.blogschool.blogs.entity.ApprovalRequestEntity;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.repository.ApprovalRequestRepository;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalRequestService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;


    public ResponseEntity<ResponseObject> getAllApprovalRequest() {
        List<ApprovalRequestEntity> requestEntityList = approvalRequestRepository.findAll();
        return requestEntityList.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "list exists", requestEntityList)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("not found", "list empty", requestEntityList));
    }


    public ApprovalRequestEntity getApprovalRequestById(Long postId) {

        return approvalRequestRepository.findById(postId).orElse(null);
    }


    public ResponseEntity<ResponseObject> createApprovalRequestById(BlogPostEntity newBlogPost) {
        Optional<UserEntity> userEntity = userRepository.findById(newBlogPost.getAuthors().getUserId());

        ApprovalRequestEntity newApprovalRequest = new ApprovalRequestEntity(false, userEntity.get(),
                null, newBlogPost);

        approvalRequestRepository.save(newApprovalRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "the post is waiting approve", ""));
    }


    public ResponseEntity<ResponseObject> approveBlogPost(Long postId, ApprovalRequestDTO approvalRequestDTO) {

        Optional<UserEntity> userEntity = userRepository.findById(approvalRequestDTO.getReviewId());
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        ApprovalRequestEntity approvalRequestEntity = approvalRequestRepository.findByBlogPost(blogPostEntity.get());

        if (blogPostEntity.isPresent()) {
            if (approvalRequestDTO.getCommand().equalsIgnoreCase("approve")) {

                UserEntity reviewer = userEntity.get();

                approvalRequestEntity.setReview(reviewer);
                approvalRequestEntity.setApproved(true);

                approvalRequestRepository.save(approvalRequestEntity);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "approved successful", ""));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "approved failed", ""));
    }
}
