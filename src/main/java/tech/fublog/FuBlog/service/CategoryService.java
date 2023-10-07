package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.response.CategoryResponseDTO;
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

    public List<CategoryResponseDTO> getAllCategory() {
        List<CategoryEntity> list = categoryRepository.findByParentCategoryIsNull();
        if (!list.isEmpty()) {
            List<CategoryResponseDTO> dtoList = new ArrayList<>();
            for (CategoryEntity entity : list) {
                CategoryResponseDTO dto = convertCategoryToDTO(entity);
                dtoList.add(dto);
            }
            return dtoList;
        }
        return new ArrayList<>();
    }

    private CategoryResponseDTO convertCategoryToDTO(CategoryEntity categoryEntity) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryId(categoryEntity.getId());
        categoryResponseDTO.setCategoryName(categoryEntity.getCategoryName());
        List<CategoryEntity> subCategory = categoryRepository.findByParentCategory(categoryEntity);
        List<CategoryResponseDTO> subcategoryDTOResponse = new ArrayList<>();
        for (CategoryEntity sub : subCategory) {
            CategoryResponseDTO subCategoryResponseDTOs = convertCategoryToDTO(sub);
            subcategoryDTOResponse.add(subCategoryResponseDTOs);
        }
        categoryResponseDTO.setSubCategory(subcategoryDTOResponse);
        return categoryResponseDTO;
    }
}
