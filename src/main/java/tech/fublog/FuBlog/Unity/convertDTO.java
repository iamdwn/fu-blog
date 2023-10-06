package tech.fublog.FuBlog.Unity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.fublog.FuBlog.dto.response.ResponseCategoryDTO;
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

    private ResponseCategoryDTO convertCategoryToDTO(CategoryEntity categoryEntity) {
        ResponseCategoryDTO responseCategoryDTO = new ResponseCategoryDTO();
        responseCategoryDTO.setCategoryId(categoryEntity.getId());
        responseCategoryDTO.setCategoryName(categoryEntity.getCategoryName());
        List<CategoryEntity> subCategory = categoryRepository.findByParentCategory(categoryEntity);
        List<ResponseCategoryDTO> subcategoryDTOResponse = new ArrayList<>();
        for (CategoryEntity sub : subCategory) {
            ResponseCategoryDTO subResponseCategoryDTOs = convertCategoryToDTO(sub);
            subcategoryDTOResponse.add(subResponseCategoryDTOs);
        }
        responseCategoryDTO.setSubCategory(subcategoryDTOResponse);
        return responseCategoryDTO;
    }
}
