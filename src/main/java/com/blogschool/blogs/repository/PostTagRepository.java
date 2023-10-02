package com.blogschool.blogs.repository;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.PostTagEntity;
import com.blogschool.blogs.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {
    Optional<PostTagEntity> findByPostAndTag(BlogPostEntity blogPostEntity, TagEntity tagEntity);

    Set<PostTagEntity> findByIdNotNull();

}
