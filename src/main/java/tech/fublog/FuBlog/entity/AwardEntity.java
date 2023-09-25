package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    private Long id;

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


}
