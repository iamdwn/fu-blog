package tech.fublog.FuBlog.service;


import tech.fublog.FuBlog.dto.request.RequestCommentDTO;
import tech.fublog.FuBlog.dto.response.ResponseCommentDTO;
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

    public void deleteComment(RequestCommentDTO requestCommentDTO) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(requestCommentDTO.getCommentId());
        if (commentEntity.isPresent()) {
            commentEntity.get().setStatus(false);
            commentRepository.save(commentEntity.get());
        } else throw new CommentException("Comment doesn't exists");
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
            userEntity.get().setPoint(userEntity.get().getPoint() +0.5);
            commentRepository.save(commentEntity);
        } else throw new CommentException("Blog or User doesn't exists");
    }

    public void updateComment(RequestCommentDTO requestCommentDTO) {
        Optional<CommentEntity> entity = commentRepository.findById(requestCommentDTO.getCommentId());
        if (entity.isPresent()) {
            entity.get().setContent(requestCommentDTO.getContent());
            commentRepository.save(entity.get());
        } else throw new CommentException("Comment doesn't exists");
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
