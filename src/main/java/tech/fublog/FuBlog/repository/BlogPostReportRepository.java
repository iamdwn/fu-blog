package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.BlogPostReportEntity;
import tech.fublog.FuBlog.entity.UserEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface BlogPostReportRepository extends JpaRepository<BlogPostReportEntity, Long> {
    BlogPostReportEntity findByUserAndBlog(UserEntity userEntity, BlogPostEntity blogPostEntity);

    List<BlogPostReportEntity> findByOrderByCreatedDateDesc();

}
