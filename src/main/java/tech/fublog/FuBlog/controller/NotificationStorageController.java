package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.dto.response.NotificationDTO;
import tech.fublog.FuBlog.entity.NotificationEntity;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.exception.NotificationException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.NotificationStorageService;

import java.util.List;
@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
//@CrossOrigin(origins = "*")
public class NotificationStorageController {
    private final NotificationStorageService notifService;

    public NotificationStorageController(NotificationStorageService notifService) {
        this.notifService = notifService;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<ResponseObject> getNotificationsByUserID(@RequestHeader("Authorization") String token,
                                                                   @PathVariable Long userID) {
            try {
                if (TokenChecker.checkToken(token)) {
                    List<NotificationDTO> notis = notifService.getNotificationsByUserID(userID);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "notification found", notis));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("failed", "not found", ""));
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("failed", ex.getMessage(), ""));
            }
    }

    @PatchMapping("/read/{notifID}")
    public ResponseEntity changeNotifStatusToRead(@RequestHeader("Authorization") String token,
                                                  @PathVariable String notifID) {
        try {
            if (TokenChecker.checkToken(token)) {
                return ResponseEntity.ok(notifService.changeNotifStatusToRead(notifID));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }


}
