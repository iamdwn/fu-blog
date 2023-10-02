package com.blogschool.blogs.repository;

import com.blogschool.blogs.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByCategoryName(String categoryName);

    Optional<CategoryEntity> findByCategoryNameAndParentCategory(String categoryName, CategoryEntity categoryEntity);

    List<CategoryEntity> findByParentCategory(CategoryEntity categoryEntity);

    List<CategoryEntity> findByParentCategoryIsNull();

    Optional<CategoryEntity> findByCategoryNameAndParentCategoryIsNull(String categoryName);
}
