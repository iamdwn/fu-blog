package tech.fublog.FuBlog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity,Long> {
    public List<BlogPostEntity> getBlogPostEntitiesByTitle(String title, Pageable pageable);
//    List<BlogPostEntity> findAllByCategory(Long id);

    Page<BlogPostEntity> findByCategory(CategoryEntity category, Pageable pageable);

    Page<BlogPostEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<BlogPostEntity> findAllByOrderByCreatedDateAsc(Pageable pageable);
    Page<BlogPostEntity> findAllByOrderByModifiedDateDesc(Pageable pageable);
    Page<BlogPostEntity> findAllByOrderByModifiedDateAsc(Pageable pageable);

}
