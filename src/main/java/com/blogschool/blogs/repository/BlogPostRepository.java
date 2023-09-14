package com.blogschool.blogs.repository;

import com.blogschool.blogs.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
//    BlogPostEntity findBlogPostById(Long Id);
}
