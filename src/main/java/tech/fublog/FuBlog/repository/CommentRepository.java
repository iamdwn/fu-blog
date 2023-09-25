package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CommentEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostComment(BlogPostEntity blogPostEntity);

    Long countByPostComment(BlogPostEntity blogPostEntity);

    CommentEntity findByPostCommentAndUserCommentAndId(BlogPostEntity blogPostEntity, UserEntity userEntity, Long id);

    List<CommentEntity> findByParentCommentIsNull();

    List<CommentEntity> findByPostCommentAndParentCommentIsNull(BlogPostEntity blogPostEntity);

    List<CommentEntity> findByParentComment(CommentEntity commentEntity);

    CommentEntity findParentCommentById(Long id);
}
