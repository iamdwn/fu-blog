package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.dto.request.BlogPostReportDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.BlogPostReportService;

@RestController
@RequestMapping("/api/v1/auth/blogReport")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
public class BlogPostReportController {
    private final BlogPostReportService blogPostReportService;

    public BlogPostReportController(BlogPostReportService blogPostReportService) {
        this.blogPostReportService = blogPostReportService;
    }

    @GetMapping("/viewAllReport")
    public ResponseEntity<ResponseObject> viewAllReport(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, true)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("200", "found", blogPostReportService.viewAllReportBlog()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/createReportBlog")
    public ResponseEntity<ResponseObject> createReportBlog(@RequestHeader("Authorization") String token,
                                                           @RequestBody BlogPostReportDTO blogPostReportDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                blogPostReportService.createReportBlog(blogPostReportDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("200", "found", true));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PutMapping("/checkReportBlog")
    public ResponseEntity<ResponseObject> checkFollow(@RequestHeader("Authorization") String token,
                                                      @RequestBody BlogPostReportDTO blogPostReportDTO) {
        try {
            if (TokenChecker.checkRole(token, true)) {
                boolean result = blogPostReportService.checkReport(blogPostReportDTO.getUserId(), blogPostReportDTO.getBlogId());
                if (result)
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "You already report this blog", true));
                else
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "You hasn't report this blog", false));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

}
