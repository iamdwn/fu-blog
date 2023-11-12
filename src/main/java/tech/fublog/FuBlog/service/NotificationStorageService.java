package tech.fublog.FuBlog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.dto.response.NotificationDTO;
import tech.fublog.FuBlog.entity.NotificationEntity;
import tech.fublog.FuBlog.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NotificationStorageService {
    private final NotificationRepository notifRepository;

    public NotificationStorageService(NotificationRepository notifRepository) {
        this.notifRepository = notifRepository;
    }

    public NotificationEntity createNotificationStorage(NotificationEntity notificationStorage) {
        return notifRepository.save(notificationStorage);
    }


    public NotificationEntity getNotificationsByID(Long id) {
        return notifRepository.findByNotificationId(id).orElseThrow(() -> new RuntimeException("notification not found!"));
    }

    public List<NotificationEntity> getNotificationsByUserIDNotRead(Long userID) {
        return notifRepository.findByUserIdAndIsDeliveredFalse(userID);
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
    public List<NotificationDTO> getNotificationsByUserID(Long userID) {
        var notifs = notifRepository.findByUserId(userID);
        return convertToDTOList(notifs);
    }

    public NotificationEntity changeNotifStatusToRead(String notifID) {
        var notif = notifRepository.findById(notifID)
                .orElseThrow(() -> new RuntimeException("not found!"));
        notif.setIsRead(true);

        return notifRepository.save(notif);
    }

    public void clear() {
        notifRepository.deleteAll();
    }
}
