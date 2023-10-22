package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.dto.request.BlogReportDTO;
import tech.fublog.FuBlog.dto.request.FollowRequestDTO;
import tech.fublog.FuBlog.exception.BlogReportException;
import tech.fublog.FuBlog.exception.FollowException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.BlogReportService;

@RestController
@RequestMapping("/api/v1/auth/blogReport")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
public class BlogReportController {
    private final BlogReportService blogReportService;

    public BlogReportController(BlogReportService blogReportService) {
        this.blogReportService = blogReportService;
    }

    @GetMapping("/viewAllReport")
    public ResponseEntity<ResponseObject> viewAllReport() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("200", "found", blogReportService.viewAllReportBlog()));
    }

    @PostMapping("/createReportBlog")
    public ResponseEntity<ResponseObject> createReportBlog(@RequestBody BlogReportDTO blogReportDTO) {
        try {
            blogReportService.createReportBlog(blogReportDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("200", "found", true));
        } catch (BlogReportException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("500", ex.getMessage(), false));
        }
    }

    @PutMapping("/checkReportBlog")
    public ResponseEntity<ResponseObject> checkFollow(@RequestBody BlogReportDTO blogReportDTO) {
        try {
            boolean result = blogReportService.checkReport(blogReportDTO.getUserId(), blogReportDTO.getBlogId());
            if (result)
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "You already report this blog", true));
            else
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "You hasn't report this blog", false));
        } catch (FollowException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}
