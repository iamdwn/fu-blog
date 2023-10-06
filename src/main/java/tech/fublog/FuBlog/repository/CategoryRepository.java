package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByCategoryName(String categoryName);

    List<CategoryEntity> findByParentCategory(Long parentId);

    Optional<CategoryEntity> findByCategoryNameAndParentCategory(String categoryName, CategoryEntity categoryEntity);

    List<CategoryEntity> findByParentCategory(CategoryEntity categoryEntity);

    List<CategoryEntity> findByParentCategoryIsNull();

    Optional<CategoryEntity> findByCategoryNameAndParentCategoryIsNull(String categoryName);

    CategoryEntity findParentCategoryById(Long id);
}
