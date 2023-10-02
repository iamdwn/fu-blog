package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.dto.ResponseBlogPostDTO;
import com.blogschool.blogs.dto.ResponseCommentDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.exception.BlogPostException;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CategoryRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Set<BlogPostDTO> findBlogByCategory(String name, Long parentCategoryId) {
        Optional<CategoryEntity> categoryEntity;
        if (parentCategoryId == null)
            categoryEntity = categoryRepository.findByCategoryNameAndParentCategoryIsNull(name);
        else
            categoryEntity = findCategoryByNameAndParentId(name, parentCategoryId);
        if (categoryEntity.isPresent()) {
            List<CategoryEntity> categoryEntityList = findCategoryToSearch(categoryEntity.get(), new ArrayList<>());
            if (!categoryEntityList.isEmpty()) {
                Set<BlogPostEntity> blogPostEntitySet = new HashSet<>();
                Set<BlogPostDTO> blogPostDTOSet = new HashSet<>();
                for (CategoryEntity entity : categoryEntityList) {
                    blogPostEntitySet.addAll(entity.getBlogPosts());
                }
                for (BlogPostEntity entity : blogPostEntitySet) {
                    if (entity.getStatus() && entity.getIsApproved())
                        blogPostDTOSet.add(convertDTO(entity));
                }
                return blogPostDTOSet;
            } else throw new BlogPostException("List empty");
        } else throw new BlogPostException("Category doesn't exists");
    }

    public List<BlogPostDTO> findBlogByTitle(String title) {
        if (!title.trim().isEmpty()) {
            List<BlogPostEntity> list = blogPostRepository.findByTitleLike("%" + title.trim() + "%");
            if (!list.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity entity : list) {
                    if (entity.getIsApproved() && entity.getStatus()) {
                        dtoList.add(convertDTO(entity));
                    }
                }
                return dtoList;
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

    public ResponseBlogPostDTO viewBlogPost(Long postId, Long vote, List<ResponseCommentDTO> comment) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            return convertResponseDTO(blogPostEntity.get(), vote, comment);
        } else throw new BlogPostException("Blog doesn't exists");
    }

    public BlogPostDTO convertDTO(BlogPostEntity blogPostEntity) {
        Long parentCategoryId = null;
        if (blogPostEntity.getCategory().getParentCategory() != null)
            parentCategoryId = blogPostEntity.getCategory().getParentCategory().getId();
        return new BlogPostDTO(blogPostEntity.getId(), blogPostEntity.getTypePost(),
                blogPostEntity.getTitle(), blogPostEntity.getContent(),
                blogPostEntity.getCategory().getCategoryName(),
                parentCategoryId, null, blogPostEntity.getAuthors().getId());
    }

    public ResponseBlogPostDTO convertResponseDTO(BlogPostEntity blogPostEntity, Long vote, List<ResponseCommentDTO> comment) {
        Long parentCategoryId = null;
        if (blogPostEntity.getCategory().getParentCategory() != null)
            parentCategoryId = blogPostEntity.getCategory().getParentCategory().getId();
        return new ResponseBlogPostDTO(blogPostEntity.getId(), blogPostEntity.getAuthors().getId(),
                blogPostEntity.getTypePost(), blogPostEntity.getTitle(),
                blogPostEntity.getContent(), blogPostEntity.getCategory().getCategoryName(),
                parentCategoryId, vote, comment);
    }

    public List<CategoryEntity> findCategoryToSearch(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntityList) {
        List<CategoryEntity> subEntityList = categoryRepository.findByParentCategory(categoryEntity);
        if (subEntityList.isEmpty()) {
            categoryEntityList.add(categoryEntity);
        } else {
            categoryEntityList.add(categoryEntity);
            for (CategoryEntity entity : subEntityList) {
                findCategoryToSearch(entity, categoryEntityList);
            }
        }
        return categoryEntityList;
    }

    public Optional<CategoryEntity> findCategoryByNameAndParentId(String name, Long parentCategoryId) {
        Optional<CategoryEntity> parentCategory = categoryRepository.findById(parentCategoryId);
        if (parentCategory.isPresent()) {
            return categoryRepository.findByCategoryNameAndParentCategory(name, parentCategory.get());
        } else return parentCategory;
    }
}
