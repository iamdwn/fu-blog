package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.exception.BlogPostException;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CategoryRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<BlogPostDTO> findBlogByCategory(String name, Long parentCategoryId) {
        Optional<CategoryEntity> category = findCategoryByNameAndParentId(name, parentCategoryId);
        if (category.isPresent()) {
            List<BlogPostEntity> list = category.get().getBlogPosts();
            if (!list.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity entity : list) {
                    if (entity.getApproved() == true && entity.getStatus() == true) {
                        BlogPostDTO dto = new BlogPostDTO(entity.getId(), entity.getTypePost(), entity.getTitle(), entity.getContent(), entity.getCategory().getCategoryName(), entity.getCategory().getParentCategory().getId(), entity.getAuthors().getId());
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
        if (!title.trim().isEmpty()) {
            List<BlogPostEntity> list = blogPostRepository.findByTitleLike("%" + title.trim() + "%");
            if (!list.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity entity : list) {
                    if (entity.getApproved() == true && entity.getStatus() == true) {
                        BlogPostDTO dto = new BlogPostDTO(entity.getId(), entity.getTypePost(), entity.getTitle(), entity.getContent(), entity.getCategory().getCategoryName(), entity.getCategory().getParentCategory().getId(), entity.getAuthors().getId());
                        dtoList.add(dto);
                    }
                }
                if (!dtoList.isEmpty()) {
                    return dtoList;
                } else throw new BlogPostException("Blog post doesn't exists");
            } else throw new BlogPostException("Cannot found any blog post with this title");
        } else throw new BlogPostException("Nothing to search");
    }

    public BlogPostEntity insertBlogPost(BlogPostDTO blogPostDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(blogPostDTO.getUserId());
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(blogPostDTO.getCategoryName(), blogPostDTO.getParentCategoryId());
        if (userEntity.isPresent() && categoryEntity.isPresent()) {
            BlogPostEntity blogPostEntity = new BlogPostEntity
                    (blogPostDTO.getTypePost(), blogPostDTO.getTitle(), blogPostDTO.getContent(), categoryEntity.get(), userEntity.get());
            return blogPostRepository.save(blogPostEntity);
        } else throw new BlogPostException("User or Category doesn't exists");
    }

    public BlogPostEntity updateBlogPost(BlogPostDTO blogPostDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(blogPostDTO.getUserId());
        Optional<CategoryEntity> categoryEntity = findCategoryByNameAndParentId(blogPostDTO.getCategoryName(), blogPostDTO.getParentCategoryId());
        if (userEntity.isPresent() && categoryEntity.isPresent()) {
            BlogPostEntity blogPostEntity = new BlogPostEntity
                    (blogPostDTO.getTypePost(), blogPostDTO.getTitle(), blogPostDTO.getContent(), categoryEntity.get(), userEntity.get());
            return blogPostRepository.save(blogPostEntity);
        } else throw new BlogPostException("User or Category doesn't exists");
    }

    public Optional<CategoryEntity> findCategoryByNameAndParentId(String name, Long parentCategoryId) {
        Optional<CategoryEntity> parentCategory = categoryRepository.findById(parentCategoryId);
        if (parentCategory.isPresent()) {
            return categoryRepository.findByCategoryNameAndParentCategory(name, parentCategory.get());
        } else return parentCategory;
    }
}
