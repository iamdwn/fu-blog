package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    public BlogPostEntity createBlogPost(BlogPostEntity blogPost) {
        return blogPostRepository.save(blogPost);
    }

    public BlogPostEntity getBlogPostById(Long id) {
        return blogPostRepository.findById(id).orElse(null);
    }

    public List<BlogPostEntity> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    public void deleteBlogPost(Long id) {
        blogPostRepository.deleteById(id);
    }
}
