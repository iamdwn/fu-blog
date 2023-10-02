package tech.fublog.FuBlog.service;


import tech.fublog.FuBlog.dto.ResponseCommentDTO;
import tech.fublog.FuBlog.entity.*;
import tech.fublog.FuBlog.entity.CommentEntity;
import tech.fublog.FuBlog.exception.CommentException;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.CommentRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.UserEntity;

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

    public void deleteComment(ResponseCommentDTO commentDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(commentDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(commentDTO.getUserId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            CommentEntity commentEntity = commentRepository.findByPostCommentAndUserCommentAndId(blogPostEntity.get(), userEntity.get(), commentDTO.getCommentId());
            if (commentEntity != null) {
                commentEntity.setStatus(false);
                commentRepository.save(commentEntity);
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
                    ResponseCommentDTO dto = convertResponseDTO(entity);
                    dtoList.add(dto);
                }
                return dtoList;
            } else return new ArrayList<>();
        } else throw new CommentException("Blog doesn't exists");
    }

    public void insertComment(ResponseCommentDTO commentDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(commentDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(commentDTO.getUserId());
        CommentEntity parentComment = commentRepository.findParentCommentById(commentDTO.getParentCommentId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            CommentEntity commentEntity = new CommentEntity(commentDTO.getContent(), userEntity.get(), blogPostEntity.get(), parentComment);
            commentRepository.save(commentEntity);
        } else throw new CommentException("Blog or User doesn't exists");
    }

    public void updateComment(ResponseCommentDTO commentDTO) {
        Optional<CommentEntity> entity = commentRepository.findById(commentDTO.getCommentId());
        if (entity.isPresent()) {
            entity.get().setContent(commentDTO.getContent());
            commentRepository.save(entity.get());
        } else throw new CommentException("Blog and User doesn't match in database");
    }

    public Long countComment(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent())
            return commentRepository.countByPostComment(blogPostEntity.get());
        else throw new CommentException("Blog doesn't exists");
    }

    private ResponseCommentDTO convertResponseDTO(CommentEntity commentEntity) {
        if (commentEntity.getStatus()) {
            ResponseCommentDTO responseCommentDTO = new ResponseCommentDTO();
            responseCommentDTO.setCommentId(commentEntity.getId());
            if (commentEntity.getParentComment() != null)
                responseCommentDTO.setParentCommentId(commentEntity.getParentComment().getId());
            responseCommentDTO.setContent(commentEntity.getContent());
            responseCommentDTO.setPostId(commentEntity.getPostComment().getId());
            responseCommentDTO.setUserId(commentEntity.getUserComment().getId());
            responseCommentDTO.setStatus(commentEntity.getStatus());
            List<CommentEntity> subComment = commentRepository.findByParentComment(commentEntity);
            List<ResponseCommentDTO> dtoList = new ArrayList<>();
            for (CommentEntity sub : subComment) {
                ResponseCommentDTO subResponseCommentDTOs = convertResponseDTO(sub);
                dtoList.add(subResponseCommentDTOs);
            }
            responseCommentDTO.setSubComment(dtoList);
            return responseCommentDTO;
        } else return null;
    }
}
