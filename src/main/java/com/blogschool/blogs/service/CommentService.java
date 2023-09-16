package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CommentEntity;
import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CommentRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseObject> viewComment(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            List<CommentEntity> list = commentRepository.findByPostComment(blogPostEntity.get());
            return list.size() > 0 ?
                    ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "comment of postId: " + postId, list)) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseObject("failed", "no comment found of postId: " + postId, ""));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "post doesn't exists", ""));
        }
    }

    public ResponseEntity<ResponseObject> insertComment(String content, Long postId, Long userId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
//            BlogPostEntity blogPost = blogPostEntity.get();
//            UserEntity user = userEntity.get();
            Date createdDate = new Date();
            CommentEntity commentEntity = new CommentEntity(content, createdDate, userEntity.get(), blogPostEntity.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "comment have been inserted", commentRepository.save(commentEntity)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("failed", "post or user doesn't exists", ""));
        }
    }
}
