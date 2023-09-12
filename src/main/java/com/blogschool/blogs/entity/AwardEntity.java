package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Award")
public class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;

    @Column
    private String awardName;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToMany
    @JoinTable(name = "UserAward",
            joinColumns = @JoinColumn(name = "award_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> users = new ArrayList<>();


}
