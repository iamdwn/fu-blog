package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> findAllCategory() {
        return categoryRepository.findAll();
    }
}
