package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.CategoryDTO;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.exception.CategoryException;
import com.blogschool.blogs.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blogPosts/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> getAll() {
//        List<CategoryDTO> dtoList = categoryService.getAllCategory();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", categoryService.getAllCategory()));
    }
}
