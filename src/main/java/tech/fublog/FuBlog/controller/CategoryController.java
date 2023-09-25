package tech.fublog.FuBlog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.dto.CategoryDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.CategoryService;

@RestController
@RequestMapping("/api/v1/auth/blogPosts")
//    @CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    @PostMapping("/category/create")
//    ResponseEntity<ResponseObject> createCategory(@RequestBody CategoryDTO categoryDTO){
//
//        return categoryService.createCategory(categoryDTO);
//    }
}
