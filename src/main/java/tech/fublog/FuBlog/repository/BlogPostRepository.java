package tech.fublog.FuBlog.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
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

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
    List<BlogPostEntity> findByIsApproved(Boolean isApproved);

    List<BlogPostEntity> findByTitleLike(String title);

    Optional<BlogPostEntity> findByPinnedIsTrue();

    public List<BlogPostEntity> getBlogPostEntitiesByTitle(String title, Pageable pageable);
//    List<BlogPostEntity> findAllByCategory(Long id);

    Page<BlogPostEntity> findByCategory(CategoryEntity category, Pageable pageable);

    Page<SortDTO> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<SortDTO> findAllByOrderByCreatedDateAsc(Pageable pageable);
    Page<SortDTO> findAllByOrderByModifiedDateDesc(Pageable pageable);
    Page<SortDTO> findAllByOrderByModifiedDateAsc(Pageable pageable);
//    Page<BlogPostEntity> findAllByOrderByViewsDesc(Pageable pageable);
    Page<SortDTO> findAllByOrderByViewsDesc(Pageable pageable);

}
