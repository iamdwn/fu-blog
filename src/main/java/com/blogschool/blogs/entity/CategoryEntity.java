package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
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
    private List<BlogPostEntity> blogPosts = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "parentCategoryId")
    private CategoryEntity parentCategory;

}
