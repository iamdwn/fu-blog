package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.fublog.FuBlog.entity.CategoryEntity;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
    List<BlogPostEntity> findByIsApproved(Boolean isApproved);

    List<BlogPostEntity> findByTitleLike(String title);

    public List<BlogPostEntity> getBlogPostEntitiesByTitle(String title, Pageable pageable);
//    List<BlogPostEntity> findAllByCategory(Long id);

    Page<BlogPostEntity> findByCategory(CategoryEntity category, Pageable pageable);

//    @Query("SELECT e FROM BlogPostEntity e ORDER BY e.createdDate DESC")
    Page<BlogPostEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<BlogPostEntity> findAllByOrderByCreatedDateAsc(Pageable pageable);
    Page<BlogPostEntity> findAllByOrderByModifiedDateDesc(Pageable pageable);
    Page<BlogPostEntity> findAllByOrderByModifiedDateAsc(Pageable pageable);
    Page<BlogPostEntity> findAllByOrderByViewDesc(Pageable pageable);

}
