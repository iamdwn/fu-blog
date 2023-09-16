package com.blogschool.blogs.repository;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    List<VoteEntity> findByPostVote (BlogPostEntity blogPost);
}
