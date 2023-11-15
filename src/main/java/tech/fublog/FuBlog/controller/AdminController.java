package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
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
    @GetMapping("/countBlogByAllCategory")
    public ResponseEntity<ResponseObject> countBlogByAllCategory(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.countBlogByAllCategory()));
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

    @GetMapping("/countBlogInYear")
    public ResponseEntity<ResponseObject> countBlogInYear(@RequestHeader("Authorization") String token) {
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

    @GetMapping("/calculateBlogReportWeightRatio")
    public ResponseEntity<ResponseObject> calculateBlogReportWeightRatio(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.calculateBlogReportWeightRatio()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/calculateUserReportWeightRatio")
    public ResponseEntity<ResponseObject> calculateUserReportWeightRatio(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.calculateUserReportWeightRatio()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/calculateBlogWeightRatio")
    public ResponseEntity<ResponseObject> calculateBlogWeight(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", adminService.calculateBlogWeightRatio()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/countAllUsers")
    public ResponseEntity<ResponseObject> countAllUsers(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                if (adminService.getCountAllUsers() == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseObject("failed", "do not have any user exit", ""));
                }
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "number of all users", adminService.getCountAllUsers()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}
