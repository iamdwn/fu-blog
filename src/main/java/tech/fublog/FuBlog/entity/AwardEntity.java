package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Award")
@EntityListeners(AuditingEntityListener.class)
public class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @OneToMany(mappedBy = "award")
    @JsonIgnore
    private List<UserAwardEntity> userAwards = new ArrayList<>();

//    @ManyToMany
//    @JoinTable(name = "UserAward",
//            joinColumns = @JoinColumn(name = "award_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<UserEntity> users = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AwardEntity award = (AwardEntity) obj;
        return Objects.equals(id, award.getId());
    }

}
