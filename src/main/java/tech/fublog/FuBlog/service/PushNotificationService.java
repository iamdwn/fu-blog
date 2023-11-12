package tech.fublog.FuBlog.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import tech.fublog.FuBlog.dto.response.NotificationDTO;
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

    public List<NotificationDTO> convertToDTOList(List<NotificationEntity> entities) {
        List<NotificationDTO> dtos = new ArrayList<>();
        for (NotificationEntity entity : entities) {
//            UserEntity user = user.getUser();
//            UserDTO userDTO = new UserDTO(user.getUserId(), user.getUsername());
            NotificationDTO dto = new NotificationDTO(entity.getNotificationId(), entity.getContent(), entity.getIsRead(), entity.isDelivered(), entity.getPostId(), entity.getCreatedDate(), entity.getUser().getId());
            dtos.add(dto);
        }
        return dtos;
    }
    private List<NotificationDTO> getNotifs(Long userID) {
        var notifs = notificationStorageRepository.findByUserIdAndIsDeliveredFalse(userID);
        notifs.forEach(x -> x.setDelivered(true));
        notificationStorageRepository.saveAll(notifs);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        notificationDTOS = convertToDTOList(notifs);

        for (int i = 0; i < notificationDTOS.size(); i++) {
            System.out.println(notificationDTOS);
        }
        return convertToDTOList(notifs);
//        return null;
    }

    public Flux<ServerSentEvent<List<NotificationDTO>>> getNotificationsByUserToID(Long userID) {

        if (userID != null && userID != 0) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<NotificationDTO>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(userID))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<NotificationDTO>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }
}
