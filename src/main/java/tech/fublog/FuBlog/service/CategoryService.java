package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.CategoryDTO;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.exception.CategoryException;
import com.blogschool.blogs.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategory() {
        List<CategoryEntity> list = categoryRepository.findByParentCategoryIsNull();
        if (!list.isEmpty()) {
            List<CategoryDTO> result = new ArrayList<>();
            for (CategoryEntity entity : list) {
                CategoryDTO categoryDTO = convertToDTO(entity);
                result.add(categoryDTO);
            }
            return result;
        } else throw new CategoryException("Nothing here");
    }

    private CategoryDTO convertToDTO(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(categoryEntity.getId());
        categoryDTO.setCategoryName(categoryEntity.getCategoryName());
        List<CategoryEntity> subCategory = categoryRepository.findByParentCategory(categoryEntity);
        List<CategoryDTO> subcategoryDTO = new ArrayList<>();
        for (CategoryEntity sub : subCategory) {
            CategoryDTO subCategoryDTOs = convertToDTO(sub);
            subcategoryDTO.add(subCategoryDTOs);
        }
        categoryDTO.setSubcategory(subcategoryDTO);
        return categoryDTO;
    }
}
