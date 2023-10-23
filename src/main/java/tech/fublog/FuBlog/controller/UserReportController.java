package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.Utility.TokenChecker;
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
    public ResponseEntity<ResponseObject> viewAllReport(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, true)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", userReportService.viewAllReportUser()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/createReportUser")
    public ResponseEntity<ResponseObject> createReport(@RequestHeader("Authorization") String token,
                                                       @RequestBody UserReportDTO userReportDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                userReportService.createReportUser(userReportDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", true));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PutMapping("/checkReportUser")
    public ResponseEntity<ResponseObject> checkFollow(@RequestHeader("Authorization") String token,
                                                      @RequestBody UserReportDTO userReportDTO) {
        try {
            if (TokenChecker.checkRole(token, true)) {
                boolean result = userReportService.checkReport(userReportDTO.getReporterId(), userReportDTO.getReportedUserId());
                if (result)
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "You already report this user", true));
                else
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "You hasn't report this user", false));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

}
