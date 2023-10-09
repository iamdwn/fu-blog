package tech.fublog.FuBlog.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.repository.Query;
import tech.fublog.FuBlog.dto.SortDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.entity.UserEntity;

import java.util.List;
import java.util.Set;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
    List<BlogPostEntity> findByIsApproved(Boolean isApproved);

    List<BlogPostEntity> findByTitleLikeAndIsApprovedTrueAndStatusTrue(String title);

    List<BlogPostEntity> getBlogPostEntitiesByTitle(String title, Pageable pageable);

    Page<BlogPostEntity> findByCategory(CategoryEntity category, Pageable pageable);

    Page<BlogPostEntity> findByCategoryInAndIsApprovedTrueAndStatusTrue(List<CategoryEntity> categoryEntityList, Pageable pageable);

    Page<BlogPostEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Page<BlogPostEntity> findAllByOrderByCreatedDateAsc(Pageable pageable);

    Page<BlogPostEntity> findAllByOrderByModifiedDateDesc(Pageable pageable);

    Page<BlogPostEntity> findAllByOrderByModifiedDateAsc(Pageable pageable);

    Page<BlogPostEntity> findAllByOrderByViewDesc(Pageable pageable);

    Set<BlogPostEntity> findByUserMarks(UserEntity userMarks);

}
