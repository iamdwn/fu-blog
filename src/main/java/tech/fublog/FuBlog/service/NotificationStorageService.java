package tech.fublog.FuBlog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.entity.NotificationEntity;
import tech.fublog.FuBlog.repository.NotificationRepository;

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


    public NotificationEntity getNotificationsByID(String id) {
        return notifRepository.findById(id).orElseThrow(() -> new RuntimeException("notification not found!"));
    }

//    public List<NotificationEntity> getNotificationsByUserIDNotRead(String userID) {
//        return notifRepository.findByUserToIdAndDeliveredFalse(userID);
//    }


//    public List<NotificationEntity> getNotificationsByUserID(String userID) {
//        return notifRepository.findByUserToNotificationId(userID);
//    }

//    public Notification changeNotifStatusToRead(String notifID) {
//        var notif = notifRepository.findById(notifID)
//                .orElseThrow(() -> new RuntimeException("not found!"));
//        notif.setRead(true);
//        return notifRepository.save(notif);
//    }

    public void clear() {
        notifRepository.deleteAll();
    }
}
