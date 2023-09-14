package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.ResponeObject;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {
    //    private final BlogPostRepository blogPostRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BlogPostService(/*BlogPostRepository blogPostRepository,*/ CategoryRepository categoryRepository) {
//        this.blogPostRepository = blogPostRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<ResponeObject> findBlogByCategory(String name) {
        CategoryEntity category = categoryRepository.findByCategoryName(name);
        if (category != null) {
            List<BlogPostEntity> list = category.getBlogPosts();
            if (!list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponeObject("ok", "found", list));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponeObject("failed", "cannot found any blog with category", ""));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponeObject("failed", "category not exists", ""));
        }
    }
}
