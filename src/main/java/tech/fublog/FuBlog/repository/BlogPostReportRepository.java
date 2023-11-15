package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.BlogPostReportEntity;
import tech.fublog.FuBlog.entity.UserEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface BlogPostReportRepository extends JpaRepository<BlogPostReportEntity, Long> {
    BlogPostReportEntity findByUserAndBlog(UserEntity userEntity, BlogPostEntity blogPostEntity);

    @Query("SELECT count(b) FROM BlogPostReportEntity b WHERE year(b.createdDate) = year(current_date) AND month(b.createdDate) = month(current_date)")
    Double  countAllInCurrentMonth();

    @Query("SELECT count(b) FROM BlogPostReportEntity b WHERE year(b.createdDate) = year(current_date) AND month(b.createdDate) = month(current_date) - 1")
    Double countAllInPreviousMonth();

    @Query("SELECT count(b) FROM BlogPostReportEntity b WHERE year(b.createdDate)= year(current_date) - 1 AND month(b.createdDate)= month(current_date) + 11")
    Double countAllInPreviousMonthAndYear();

    @Query("SELECT bp FROM BlogPostReportEntity bp JOIN ApprovalRequestEntity a WHERE bp.blog.isApproved = true")
    List<BlogPostReportEntity> findByOrderByCreatedDateDesc();

    @Query("SELECT bp FROM BlogPostReportEntity bp JOIN ApprovalRequestEntity a WHERE bp.blog.id = :postId AND bp.blog.isApproved = false AND a.review.id != null")
    BlogPostReportEntity findBlogRejectedById(@Param("postId") Long postId);

}
