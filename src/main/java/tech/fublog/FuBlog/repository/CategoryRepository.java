package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.UserEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByCategoryName(String categoryName);

    @Query("SELECT COUNT(f) FROM CategoryEntity f WHERE f.categoryName = :category_name AND f.parentCategory = :parent_category_id")
    Long findCategoryWithNameAndParent(@Param("category_name") String categoryName,
                                      @Param("parent_category_id") CategoryEntity parentCategoryId);

}
