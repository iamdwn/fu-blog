package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.ApprovalRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequestEntity, Long> {
}
