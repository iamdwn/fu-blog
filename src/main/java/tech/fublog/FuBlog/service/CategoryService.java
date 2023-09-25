package tech.fublog.FuBlog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.dto.CategoryDTO;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    public ResponseEntity<ResponseObject> createCategory(CategoryDTO categoryDTO) {
//        CategoryEntity category = categoryRepository.findByCategoryName(categoryDTO.getName());
//
//        if (category.getBlogPosts().isEmpty()) {
//
//            if (categoryRepository
//                    .findCategoryWithNameAndParent(categoryDTO.getName(), category.getParentCategory()) < 1) {
//
//                CategoryEntity categoryEntity = new CategoryEntity(categoryDTO.getName(), null,
//                        category.getParentCategory());
//
//                categoryRepository.save(categoryEntity);
//
//                return ResponseEntity.status(HttpStatus.OK)
//                        .body(new ResponseObject("success", "created", ""));
//            }
//        }
//
//
//        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
//                .body(new ResponseObject("failed", "created failed", ""));
//    }
}
