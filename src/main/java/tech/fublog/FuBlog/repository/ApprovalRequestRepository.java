package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.ApprovalRequestEntity;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequestEntity, Long> {
    ApprovalRequestEntity findByBlogPost(BlogPostEntity blogPost);
}
