package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.NotificationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

    Optional<NotificationEntity> findByNotificationId(Long id);

    List<NotificationEntity> findByUserId(Long id);


    List<NotificationEntity> findByUserIdAndIsDeliveredFalse(Long id);


}
