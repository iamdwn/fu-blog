package com.blogschool.blogs.repository;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CommentEntity;
import com.blogschool.blogs.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostComment(BlogPostEntity blogPostEntity);

    Long countByPostComment(BlogPostEntity blogPostEntity);

//    CommentEntity findByPostCommentAndUserCommentAndId(BlogPostEntity blogPostEntity, UserEntity userEntity, Long id);
}
