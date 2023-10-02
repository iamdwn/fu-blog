package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Award")
public class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @OneToMany(mappedBy = "award")
    private Set<UserAwardEntity> userAwards = new HashSet<>();

//    @ManyToMany
//    @JoinTable(name = "UserAward",
//            joinColumns = @JoinColumn(name = "award_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<UserEntity> users = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AwardEntity award = (AwardEntity) obj;
        return Objects.equals(Id, award.getId());
    }

}
