package tech.fublog.FuBlog.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tech.fublog.FuBlog.entity.NotificationEntity;
import tech.fublog.FuBlog.service.PushNotificationService;

import java.util.List;

@RestController
@RequestMapping("/push-notifications")
@Slf4j
public class PushNotificationController {

    private final PushNotificationService service;

    public PushNotificationController(PushNotificationService service) {
        this.service = service;
    }

    @GetMapping("/{userID}")
    public Flux<ServerSentEvent<List<NotificationEntity>>> streamLastMessage(@PathVariable Long userID) {
        return service.getNotificationsByUserToID(userID);
    }

}
