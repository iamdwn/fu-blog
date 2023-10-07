package tech.fublog.FuBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/blogPosts/category")
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/viewAll")
    public ResponseEntity<ResponseObject> getAllCategory() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", categoryService.getAllCategory()));
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> getCategory() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", categoryService.getCategory()));
    }
}
