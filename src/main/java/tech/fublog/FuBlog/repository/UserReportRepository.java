package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.entity.UserReportEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReportEntity, Long> {
    UserReportEntity findByReporterIdAndReportedUserId(UserEntity reporter, UserEntity reported);

    List<UserReportEntity> findByOrderByCreatedDateDesc();
}
