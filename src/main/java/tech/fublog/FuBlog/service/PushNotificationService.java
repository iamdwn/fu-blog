package tech.fublog.FuBlog.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import tech.fublog.FuBlog.entity.NotificationEntity;
import tech.fublog.FuBlog.repository.NotificationRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PushNotificationService {


    private final NotificationRepository notificationStorageRepository;


    public PushNotificationService(NotificationRepository notificationStorageRepository) {
        this.notificationStorageRepository = notificationStorageRepository;
    }

    private List<NotificationEntity> getNotifs(Long userID) {
        var notifs = notificationStorageRepository.findByUserIdAndIsDeliveredFalse(userID);
        notifs.forEach(x -> x.setDelivered(true));
        notificationStorageRepository.saveAll(notifs);
        return notifs;
//        return null;
    }

    public Flux<ServerSentEvent<List<NotificationEntity>>> getNotificationsByUserToID(Long userID) {

        if (userID != null && userID != 0) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<NotificationEntity>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(userID))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<NotificationEntity>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }
}
