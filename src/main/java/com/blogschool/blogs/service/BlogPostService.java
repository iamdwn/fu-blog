package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.entity.UserEntity;
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

    public ResponseEntity<ResponseObject> findBlogByCategory(String name) {
        Optional<CategoryEntity> category = categoryRepository.findByCategoryName(name);
        if (category != null) {
            List<BlogPostEntity> list = category.get().getBlogPosts();
            if (!list.isEmpty()) {
                List<BlogPostEntity> postEntityList = new ArrayList<>();
                for (BlogPostEntity blogPost : list) {
                    if (blogPost.getApproved() == true) {
                        postEntityList.add(blogPost);
                    }
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", postEntityList));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("failed", "cannot found any blog with category", ""));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("failed", "category not exists", ""));
        }
    }

    public BlogPostEntity insertBlogPost(BlogPostDTO blogPostDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(blogPostDTO.getUserId());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByCategoryName(blogPostDTO.getCategoryName());
        if (userEntity.isPresent() && categoryEntity.isPresent()) {
            BlogPostEntity blogPostEntity = new BlogPostEntity
                    (blogPostDTO.getTypePost(), blogPostDTO.getTitle(), blogPostDTO.getContent(), categoryEntity.get(), userEntity.get());
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("ok", "post has inserted", blogPostRepository.save(blogPostEntity)));
            return blogPostRepository.save(blogPostEntity);
        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseObject("failed", "user or category doesn't exists", ""));
            return null;
        }
    }
}
