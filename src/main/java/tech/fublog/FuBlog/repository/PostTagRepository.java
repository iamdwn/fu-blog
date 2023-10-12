package tech.fublog.FuBlog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.PostTagEntity;
import tech.fublog.FuBlog.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {
    Optional<PostTagEntity> findByPostAndTag(BlogPostEntity blogPostEntity, TagEntity tagEntity);

    Page<PostTagEntity> findByTag(TagEntity tagEntity, Pageable pageable);

    Set<PostTagEntity> findByIdNotNull();

}
