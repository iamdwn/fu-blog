package com.blogschool.blogs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
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
    @JoinColumn(name = "create_by")
    @JsonIgnore
    private UserEntity category;

    public UserEntity getCategory() {
        return category;
    }

    public void setCategory(UserEntity category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return Id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<BlogPostEntity> getBlogPosts() {
        return blogPosts;
    }

    public void setBlogPosts(List<BlogPostEntity> blogPosts) {
        this.blogPosts = blogPosts;
    }

    public CategoryEntity() {
    }

    public CategoryEntity(String categoryName, List<BlogPostEntity> blogPosts, UserEntity category) {
        this.categoryName = categoryName;
        this.blogPosts = blogPosts;
        this.category = category;
    }
}
