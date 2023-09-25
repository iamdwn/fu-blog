package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.CommentDTO;
import com.blogschool.blogs.dto.ResponseCategoryDTO;
import com.blogschool.blogs.dto.ResponseCommentDTO;
import com.blogschool.blogs.entity.*;
import com.blogschool.blogs.exception.CommentException;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CommentRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void deleteComment(CommentDTO commentDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(commentDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(commentDTO.getUserId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            CommentEntity commentEntity = commentRepository.findByPostCommentAndUserCommentAndId(blogPostEntity.get(), userEntity.get(), commentDTO.getCommentId());
            if (commentEntity != null) {
                commentRepository.delete(commentEntity);
            } else throw new CommentException("Comment doesn't exists");

        } else throw new CommentException("User or Blog doesn't exists");
    }

    public List<ResponseCommentDTO> viewComment(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            List<CommentEntity> list = commentRepository.findByPostCommentAndParentCommentIsNull(blogPostEntity.get());
            if (!list.isEmpty()) {
                List<ResponseCommentDTO> dtoList = new ArrayList<>();
                for (CommentEntity entity : list) {
                    ResponseCommentDTO dto = convertToDTO(entity);
                    dtoList.add(dto);
                }
                return dtoList;
            } else throw new CommentException("Comment doesn't exists");
        } else throw new CommentException("Blog doesn't exists");
    }

    private ResponseCommentDTO convertToDTO(CommentEntity commentEntity) {
        ResponseCommentDTO responseCommentDTO = new ResponseCommentDTO();
        responseCommentDTO.setCommentId(commentEntity.getId());
        if (commentEntity.getParentComment() != null)
            responseCommentDTO.setParentCommentId(commentEntity.getParentComment().getId());
        responseCommentDTO.setContent(commentEntity.getContent());
        responseCommentDTO.setPostId(commentEntity.getPostComment().getId());
        responseCommentDTO.setUserId(commentEntity.getUserComment().getId());
        List<CommentEntity> subComment = commentRepository.findByParentComment(commentEntity);
        List<ResponseCommentDTO> dtoList = new ArrayList<>();
        for (CommentEntity sub : subComment) {
            ResponseCommentDTO subResponseCommentDTOs = convertToDTO(sub);
            dtoList.add(subResponseCommentDTOs);
        }
        responseCommentDTO.setSubComment(dtoList);
        return responseCommentDTO;
    }

    public void insertComment(CommentDTO commentDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(commentDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(commentDTO.getUserId());
        CommentEntity parentComment = commentRepository.findParentCommentById(commentDTO.getParentCommentId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            CommentEntity commentEntity = new CommentEntity(commentDTO.getContent(), userEntity.get(), blogPostEntity.get(), parentComment);
            commentRepository.save(commentEntity);
        } else throw new CommentException("Blog or User doesn't exists");
    }

    public void updateComment(CommentDTO commentDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(commentDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(commentDTO.getUserId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            CommentEntity entity = commentRepository.findByPostCommentAndUserCommentAndId(blogPostEntity.get(), userEntity.get(), commentDTO.getCommentId());
            if (entity != null) {
                entity.setContent(commentDTO.getContent());
                commentRepository.save(entity);
            } else throw new CommentException("Blog and User doesn't match in database");
        } else throw new CommentException("Blog or User doesn't exists");
    }

    public Long countComment(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent())
            return commentRepository.countByPostComment(blogPostEntity.get());
        else throw new CommentException("Blog doesn't exists");
    }
}
