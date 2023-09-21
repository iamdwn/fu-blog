package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.exception.BlogPostException;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CategoryRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    //    private final BlogPostRepository blogPostRepository;

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

//    public List<BlogPostEntity> findByApproved() {
//        List<BlogPostEntity> blogPostEntityList = blogPostRepository.findByIsApproved(false);
//        if (blogPostEntityList.size() > 0) {
//            return blogPostEntityList;
//        } else {
//            return null;
//        }
//    }

    public List<BlogPostDTO> findBlogByCategory(String name) {
        Optional<CategoryEntity> category = categoryRepository.findByCategoryName(name);
        if (category.isPresent()) {
            List<BlogPostEntity> list = category.get().getBlogPosts();
            if (!list.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity blogPost : list) {
                    if (blogPost.getApproved() == true && blogPost.getStatus() == true) {
                        BlogPostDTO dto = new BlogPostDTO(blogPost.getTypePost(), blogPost.getTitle(), blogPost.getContent(), blogPost.getCategory().getCategoryName(), blogPost.getAuthors().getId());
                        dtoList.add(dto);
                    }
                }
                if (!dtoList.isEmpty()) {
                    return dtoList;
                } else throw new BlogPostException("Blog post doesn't exists");
            } else throw new BlogPostException("Cannot found any blog post with category");
        } else throw new BlogPostException("Category doesn't exists");
    }

    public List<BlogPostDTO> findBlogByTitle(String title) {
        List<BlogPostEntity> list = blogPostRepository.findByTitleContaining(title);
        if (!list.isEmpty()) {
            List<BlogPostDTO> dtoList = new ArrayList<>();
            for (BlogPostEntity blogPost : list) {
                if (blogPost.getApproved() == true && blogPost.getStatus() == true) {
                    BlogPostDTO dto = new BlogPostDTO(blogPost.getTypePost(), blogPost.getTitle(), blogPost.getContent(), blogPost.getCategory().getCategoryName(), blogPost.getAuthors().getId());
                    dtoList.add(dto);
                }
            }
            if (!dtoList.isEmpty()) {
                return dtoList;
            } else throw new BlogPostException("Blog post doesn't exists");
        } else throw new BlogPostException("Cannot found any blog post with this title");
    }

    public BlogPostEntity insertBlogPost(BlogPostDTO blogPostDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(blogPostDTO.getUserId());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByCategoryName(blogPostDTO.getCategoryName());
        if (userEntity.isPresent() && categoryEntity.isPresent()) {
            BlogPostEntity blogPostEntity = new BlogPostEntity
                    (blogPostDTO.getTypePost(), blogPostDTO.getTitle(), blogPostDTO.getContent(), categoryEntity.get(), userEntity.get());
            return blogPostRepository.save(blogPostEntity);
        } else throw new BlogPostException("User or Category doesn't exists");
    }
}
