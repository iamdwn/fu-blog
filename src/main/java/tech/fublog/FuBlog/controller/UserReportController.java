package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.dto.request.UserReportDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.UserReportService;

@RestController
@RequestMapping("/api/v1/auth/userReport")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
public class UserReportController {
    private final UserReportService userReportService;

    public UserReportController(UserReportService userReportService) {
        this.userReportService = userReportService;
    }

    @GetMapping("/viewAllReport")
    public ResponseEntity<ResponseObject> viewAllReport() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userReportService.getAllReport()));
    }

    @PostMapping("/createReport")
    public ResponseEntity<ResponseObject> createReport(@RequestBody UserReportDTO userReportDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userReportService.createReport(userReportDTO)));
    }
}
