package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.fublog.FuBlog.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
