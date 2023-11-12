package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.entity.UserReportEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReportEntity, Long> {
    UserReportEntity findByReporterIdAndReportedUserId(UserEntity reporter, UserEntity reported);

    @Query("SELECT count(us) FROM UserReportEntity us WHERE year(us.createdDate) = year(current_date) AND month(us.createdDate) = month(current_date)")
    Double countAllInCurrentMonth();

    @Query("SELECT count(us) FROM UserReportEntity us WHERE year(us.createdDate) = year(current_date) AND month(us.createdDate) = month(current_date) - 1")
    Double countAllInPreviousMonth();

    @Query("SELECT count(us) FROM UserReportEntity us WHERE year(us.createdDate) = year(current_date) - 1 AND month(us.createdDate) = month(current_date) + 11")
    Double countAllInPreviousMonthAndYear();

    List<UserReportEntity> findByOrderByCreatedDateDesc();
}
