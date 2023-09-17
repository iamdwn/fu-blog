package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.ApprovalRequestEntity;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.repository.ApprovalRequestRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalRequestService {
    private final ApprovalRequestRepository approvalRequestRepository;

    private final UserRepository userRepository;

    @Autowired
    public ApprovalRequestService(ApprovalRequestRepository approvalRequestRepository, UserRepository userRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseObject> getAllApprovalRequest() {
        List<ApprovalRequestEntity> requestEntityList = approvalRequestRepository.findAll();
        return requestEntityList.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "list exists", requestEntityList)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("not found", "list empty", requestEntityList));
    }

    //    public ResponseEntity<ResponseObject> insertApprovalRequest()
    public ResponseEntity<ResponseObject> insertApprovalRequest(Long userId, BlogPostEntity blogPostEntity) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent() && blogPostEntity != null) {
            ApprovalRequestEntity approvalRequestEntity = new ApprovalRequestEntity(userEntity.get(), blogPostEntity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post have been waiting approve", approvalRequestRepository.save(approvalRequestEntity)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("failed", "user or blogpost doesn't exists", ""));
        }
    }
}
