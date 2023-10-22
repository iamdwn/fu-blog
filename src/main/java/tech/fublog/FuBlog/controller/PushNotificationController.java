package tech.fublog.FuBlog.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import tech.fublog.FuBlog.dto.response.NotificationDTO;
import tech.fublog.FuBlog.entity.NotificationEntity;
import tech.fublog.FuBlog.service.PushNotificationService;

import java.util.List;

@RestController
@RequestMapping("/push-notifications")
@Slf4j
//@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
@CrossOrigin(origins = "*")
public class PushNotificationController {

    private final PushNotificationService service;

    public PushNotificationController(PushNotificationService service) {
        this.service = service;
    }

//    @GetMapping("/{userID}")
//    public Flux<ServerSentEvent<List<NotificationDTO>>> streamLastMessage(@PathVariable Long userID) {
//        return service.getNotificationsByUserToID(userID);
//    }
    @GetMapping("/{userID}")
    public Flux<ServerSentEvent<List<NotificationDTO>>> streamLastMessage(@RequestHeader("Authorization") String token,
                                                                          @PathVariable Long userID) {
        return service.getNotificationsByUserToID(userID);
    }
}
