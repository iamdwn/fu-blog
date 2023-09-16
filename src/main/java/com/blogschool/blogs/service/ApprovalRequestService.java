package com.blogschool.blogs.service;


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

import java.util.Optional;

@Service
public class ApprovalRequestService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;

    public ApprovalRequestEntity getApprovalRequestById(Long postId) {

        return approvalRequestRepository.findById(postId).orElse(null);
    }


    public void createApprovalRequestById(BlogPostEntity newBlogPost, UserEntity reviewer) {

        ApprovalRequestEntity newApprovalRequest = new ApprovalRequestEntity(false, reviewer,
                null, newBlogPost);
        approvalRequestRepository.save(newApprovalRequest);
    }


    public ResponseEntity<ResponseObject> approveBlogPost(Long postId, Long reviewId, String command) {

        Optional<UserEntity> userEntity = userRepository.findById(reviewId);
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        ApprovalRequestEntity approvalRequestEntity = this.getApprovalRequestById(postId);
        if (blogPostEntity.isPresent()) {
            if (command.equalsIgnoreCase("approve")) {

                UserEntity reviewer = userEntity.get();

                approvalRequestEntity.setReview(reviewer);
                approvalRequestEntity.setApproved(true);

                approvalRequestRepository.save(approvalRequestEntity);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "APPROVED SUCCESSFUL", approvalRequestEntity));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "APPROVED FAILED", ""));
    }
}
