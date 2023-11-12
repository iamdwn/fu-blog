package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.AdminService;
import tech.fublog.FuBlog.service.BlogPostService;

@RestController
@RequestMapping("/api/v1/auth/admin/")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/countBlogByCategory/{categoryId}")
    public ResponseEntity<ResponseObject> countBlogByCategory(@RequestHeader("Authorization") String token,
                                                              @PathVariable Long categoryId) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.countBlogByCategory(categoryId)));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/countBlogInMonth")
    public ResponseEntity<ResponseObject> countBlogInMonth(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.countBlogInMonth()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/calculateBlogReportWeight")
    public ResponseEntity<ResponseObject> calculateBlogReportWeight(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.calculateBlogReportWeight()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/calculateUserReportWeight")
    public ResponseEntity<ResponseObject> calculateUserReportWeight(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.calculateUserReportWeight()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }


}
