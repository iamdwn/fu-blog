package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.UserReportEntity;

@Repository
public interface UserReportRepository extends JpaRepository<UserReportEntity, Long> {
}
