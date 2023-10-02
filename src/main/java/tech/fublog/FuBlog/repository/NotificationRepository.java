package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.NotificationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

    Optional<NotificationEntity> findBynotificationId(String id);

//    List<NotificationEntity> findByUserToNotificationId(String id);


//    List<NotificationEntity> findByUserToIdAndDeliveredFalse(String id);


}
