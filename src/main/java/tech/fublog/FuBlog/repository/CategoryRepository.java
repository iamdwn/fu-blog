package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.fublog.FuBlog.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String categoryName);

    Optional<CategoryEntity> findByNameAndParentCategory(String categoryName, CategoryEntity categoryEntity);

    List<CategoryEntity> findByParentCategory(CategoryEntity categoryEntity);

    @Query("SELECT c from CategoryEntity c where c.parentCategory.id = :categoryId")
    CategoryEntity findByParentCategory(@Param ("categoryId") Long categoryId);

    List<CategoryEntity> findByParentCategoryIsNull();

    Optional<CategoryEntity> findByNameAndParentCategoryIsNull(String categoryName);

    CategoryEntity findParentCategoryById(Long id);
}
