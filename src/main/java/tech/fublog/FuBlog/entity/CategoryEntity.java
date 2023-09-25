package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
//@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String categoryName;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<BlogPostEntity> blogPosts = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parentCategoryId")
    private CategoryEntity parentCategory;

    public Long getId() {
        return Id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<BlogPostEntity> getBlogPosts() {
        return blogPosts;
    }

    public CategoryEntity getParentCategory() {
        return parentCategory;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setBlogPosts(List<BlogPostEntity> blogPosts) {
        this.blogPosts = blogPosts;
    }

    public void setParentCategory(CategoryEntity parentCategory) {
        this.parentCategory = parentCategory;
    }

    public CategoryEntity(String categoryName, CategoryEntity parentCategory) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
    }

    public CategoryEntity(String categoryName, List<BlogPostEntity> blogPosts, CategoryEntity parentCategory) {
        this.categoryName = categoryName;
        this.blogPosts = blogPosts;
        this.parentCategory = parentCategory;
    }
}
