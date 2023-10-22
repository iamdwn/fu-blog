package tech.fublog.FuBlog.Unity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.fublog.FuBlog.dto.response.CategoryResponseDTO;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class convertDTO {
    private final CategoryRepository categoryRepository;

    @Autowired
    public convertDTO(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryResponseDTO convertCategoryToDTO(CategoryEntity categoryEntity) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryId(categoryEntity.getId());
        categoryResponseDTO.setCategoryName(categoryEntity.getName());
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
