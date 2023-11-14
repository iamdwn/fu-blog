package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.fublog.FuBlog.entity.ApprovalRequestEntity;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.CategoryEntity;

import java.util.List;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequestEntity, Long> {
    ApprovalRequestEntity findByBlogPost(BlogPostEntity blogPost);

    @Query("SELECT a FROM ApprovalRequestEntity a WHERE a.isApproved = false AND a.review.id = null")
    List<ApprovalRequestEntity> findAllByApprovedIsFalse();

    @Query("SELECT a FROM ApprovalRequestEntity a JOIN BlogPostEntity bp WHERE bp.category IN:categoryEntityList AND " +
            "a.isApproved = false AND a.review.id = null")
    List<ApprovalRequestEntity> findByCategoryInAndIsApprovedFalse(@Param("categoryEntityList") List<CategoryEntity> categoryEntityList);


}
