package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.Utility.DTOConverter;
import tech.fublog.FuBlog.dto.CategoryDTO;
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
                CategoryResponseDTO dto = DTOConverter.convertResponseCategoryToDTO(entity);
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
                CategoryDTO dto = DTOConverter.convertCategoryToDTO(entity);
                dtoList.add(dto);
            }
            return dtoList;
        }
        return new ArrayList<>();
    }

}
