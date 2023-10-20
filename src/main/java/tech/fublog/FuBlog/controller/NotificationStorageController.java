package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ResponseObject> getNotificationsByUserID(@PathVariable Long userID) {
        try {
        List<NotificationDTO> notis = notifService.getNotificationsByUserID(userID);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "notification found", notis));
        } catch (NotificationException ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PatchMapping("/read/{notifID}")
    public ResponseEntity changeNotifStatusToRead(@PathVariable String notifID) {
        return ResponseEntity.ok(notifService.changeNotifStatusToRead(notifID));
    }


}
