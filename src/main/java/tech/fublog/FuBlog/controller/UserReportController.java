package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.dto.request.BlogReportDTO;
import tech.fublog.FuBlog.dto.request.UserReportDTO;
import tech.fublog.FuBlog.exception.FollowException;
import tech.fublog.FuBlog.exception.UserReportException;
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
                .body(new ResponseObject("ok", "found", userReportService.viewAllReportUser()));
    }

    @PostMapping("/createReportUser")
    public ResponseEntity<ResponseObject> createReport(@RequestBody UserReportDTO userReportDTO) {
        try {
            userReportService.createReportUser(userReportDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", true));
        } catch (UserReportException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("500", ex.getMessage(), false));
        }
    }

    @PutMapping("/checkReportUser")
    public ResponseEntity<ResponseObject> checkFollow(@RequestBody UserReportDTO userReportDTO) {
        try {
            boolean result = userReportService.checkReport(userReportDTO.getReporterId(), userReportDTO.getReportedUserId());
            if (result)
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "You already report this user", true));
            else
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "You hasn't report this user", false));
        } catch (FollowException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}
