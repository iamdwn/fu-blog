package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.BlogPostReportEntity;

@Repository
public interface BlogPostReportRepository extends JpaRepository<BlogPostReportEntity, Long> {
}
