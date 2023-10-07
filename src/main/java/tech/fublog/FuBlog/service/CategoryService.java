package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.CategoryDTO;
import tech.fublog.FuBlog.dto.response.ResponseCategoryDTO;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.repository.CategoryRepository;
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

    public List<ResponseCategoryDTO> getAllCategory() {
        List<CategoryEntity> list = categoryRepository.findByParentCategoryIsNull();
        if (!list.isEmpty()) {
            List<ResponseCategoryDTO> dtoList = new ArrayList<>();
            for (CategoryEntity entity : list) {
                ResponseCategoryDTO dto = convertResponseCategoryToDTO(entity);
                dtoList.add(dto);
            }
            return dtoList;
        }
        return new ArrayList<>();
    }

    public List<CategoryDTO> getCategory() {
        List<CategoryEntity> list = categoryRepository.findAll();
        if (!list.isEmpty()) {
            List<CategoryDTO> dtoList = new ArrayList<>();
            for (CategoryEntity entity : list) {
                CategoryDTO dto = convertCategoryToDTO(entity);
                dtoList.add(dto);
            }
            return dtoList;
        }
        return new ArrayList<>();
    }

    private CategoryDTO convertCategoryToDTO(CategoryEntity categoryEntity) {
        CategoryDTO CategoryDTO = new CategoryDTO();
        CategoryDTO.setCategoryId(categoryEntity.getId());
        CategoryDTO.setCategoryName(categoryEntity.getCategoryName());
        if (categoryEntity.getParentCategory() != null)
            CategoryDTO.setParentCategoryId(categoryEntity.getParentCategory().getId());
        return CategoryDTO;
    }

    private ResponseCategoryDTO convertResponseCategoryToDTO(CategoryEntity categoryEntity) {
        ResponseCategoryDTO responseCategoryDTO = new ResponseCategoryDTO();
        responseCategoryDTO.setCategoryId(categoryEntity.getId());
        responseCategoryDTO.setCategoryName(categoryEntity.getCategoryName());
        if (categoryEntity.getParentCategory() != null)
            responseCategoryDTO.setParentCategoryId(categoryEntity.getParentCategory().getId());
        List<CategoryEntity> subCategory = categoryRepository.findByParentCategory(categoryEntity);
        List<ResponseCategoryDTO> subcategoryDTOResponse = new ArrayList<>();
        for (CategoryEntity sub : subCategory) {
            ResponseCategoryDTO subResponseCategoryDTOs = convertResponseCategoryToDTO(sub);
            subcategoryDTOResponse.add(subResponseCategoryDTOs);
        }
        responseCategoryDTO.setSubCategory(subcategoryDTOResponse);
        return responseCategoryDTO;
    }
}
