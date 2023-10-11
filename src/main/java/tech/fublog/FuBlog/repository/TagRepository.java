package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findByTagName(String tagName);
}
