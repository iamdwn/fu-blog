package com.blogschool.blogs.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;

    @Column
    private String tagName;

    @ManyToMany
    @JoinTable(name = "PostTag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<BlogPostEntity> blogPosts = new ArrayList<>();

    public Long getTagId() {
        return Id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<BlogPostEntity> getBlogPosts() {
        return blogPosts;
    }

    public void setBlogPosts(List<BlogPostEntity> blogPosts) {
        this.blogPosts = blogPosts;
    }
}
