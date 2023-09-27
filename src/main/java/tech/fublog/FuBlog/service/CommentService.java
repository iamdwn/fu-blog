package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.CommentDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CommentEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.CommentException;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.CommentRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            Optional<CommentEntity> commentEntity = commentRepository.findById(commentDTO.getCommentId());
            if (commentEntity.isPresent()) {
                commentRepository.delete(commentEntity.get());
            } else throw new CommentException("Comment doesn't exists");

        } else throw new CommentException("User or Blog doesn't exists");
    }

    public List<CommentDTO> viewComment(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            List<CommentEntity> list = commentRepository.findByPostComment(blogPostEntity.get());
            if (!list.isEmpty()) {
                List<CommentDTO> dtoList = new ArrayList<>();
                for (CommentEntity entity : list) {
                    CommentDTO dto = new CommentDTO(entity.getId(), entity.getContent(), entity.getPostComment().getId(), entity.getUserComment().getId());
                    dtoList.add(dto);
                }
                return dtoList;
            } else throw new CommentException("Comment doesn't exists");
        } else throw new CommentException("Blog doesn't exists");
    }

    public void insertComment(CommentDTO commentDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(commentDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(commentDTO.getUserId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            Double point = userEntity.get().getPoint();
            userEntity.get().setPoint(point + 0.5);
            CommentEntity commentEntity = new CommentEntity(commentDTO.getContent(), userEntity.get(), blogPostEntity.get());
            commentRepository.save(commentEntity);
        } else throw new CommentException("Blog or User doesn't exists");
    }

    public void updateComment(CommentDTO commentDTO) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentDTO.getCommentId());
        if (commentEntity.isPresent()) {
            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(commentDTO.getPostId());
            Optional<UserEntity> userEntity = userRepository.findById(commentDTO.getUserId());
            if (blogPostEntity.isPresent() && userEntity.isPresent()) {
                CommentEntity updateComment = commentEntity.get();
                updateComment.setContent(commentDTO.getContent());
                commentRepository.save(updateComment);
            } else throw new CommentException("Blog or User doesn't exists");
        } else throw new CommentException("Comment doesn't exists");
    }

    public Long countComment(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            Long count = commentRepository.countByPostComment(blogPostEntity.get());
            return count;
        } else throw new CommentException("Blog doesn't exists");
    }
}
