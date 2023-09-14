package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.ApprovalRequestEntity;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.repository.ApprovalRequestRepository;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CategoryRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;


    public List<BlogPostEntity> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }


    public BlogPostEntity getBlogPostById(Long id) {
        return blogPostRepository.findById(id).orElse(null);
    }


    public BlogPostEntity deleteBlogPost(Long postId) {
        BlogPostEntity blogPostEntity = this.getBlogPostById(postId);

        blogPostEntity.setStatus(false);
        return blogPostRepository.save(blogPostEntity);

    }

    public BlogPostEntity approveBlogPost(Long postId, Long approveId) {
//        Optional<ApprovalRequestEntity> approvalRequestEntity = approvalRequestRepository.findById(approveId);
//        ApprovalRequestEntity approvalRequest = approvalRequestEntity.get();

        BlogPostEntity blogPostEntity = this.getBlogPostById(postId);

        blogPostEntity.setApprovedBy(approveId);
        blogPostEntity.setApproved(true);

        return blogPostRepository.save(blogPostEntity);

    }


    public BlogPostEntity createBlogPost(String typePost, String title, String content,
                                         Long categoryId, Long authorsId) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        Optional<UserEntity> userEntity = userRepository.findById(authorsId);

        if (categoryEntity.isPresent()
                && userEntity.isPresent()) {

            CategoryEntity category = categoryEntity.get();
            UserEntity authors = userEntity.get();
            Date createdDate = new Date();

            BlogPostEntity newBlogPost = new BlogPostEntity(typePost, title, content, createdDate, null,
                    null, false, category, authors, null, true);
            return blogPostRepository.save(newBlogPost);
        }
        return null;
    }


    public BlogPostEntity updateBlogPost(Long postId, String typePost, String title, String content,
                                         Long categoryId, Long authorsModified) {

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        Optional<UserEntity> userEntity = userRepository.findById(authorsModified);
        BlogPostEntity blogPostEntity = this.getBlogPostById(postId);

        if (categoryEntity.isPresent()
                && userEntity.isPresent()) {

            CategoryEntity category = categoryEntity.get();
            UserEntity authors = userEntity.get();
            Date modifiedDate = new Date();

            blogPostEntity.setTypePost(typePost);
            blogPostEntity.setTitle(title);
            blogPostEntity.setContent(content);
            blogPostEntity.setModifiedDate(modifiedDate);
            blogPostEntity.setCategory(category);
            blogPostEntity.setAuthorsModified(authors);

            return blogPostRepository.save(blogPostEntity);
        }
        return null;
    }


}
