package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryEntity findCategoryByName(String name) {
        CategoryEntity categoryEntity = categoryRepository.findByCategoryName(name);
        if (categoryEntity == null) {
            return categoryEntity;
        }else{
            return categoryEntity;
        }
    }
}
