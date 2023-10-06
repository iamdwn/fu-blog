package tech.fublog.FuBlog.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UserAward")
@EntityListeners(AuditingEntityListener.class)
public class UserAwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreatedDate
    private Date achievementDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "award_id")
    private AwardEntity award;

    public UserAwardEntity(Date achievementDate, UserEntity user, AwardEntity award) {
        this.achievementDate = achievementDate;
        this.user = user;
        this.award = award;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserAwardEntity userAward = (UserAwardEntity) obj;
        return Objects.equals(id, userAward.getId());
    }
}
