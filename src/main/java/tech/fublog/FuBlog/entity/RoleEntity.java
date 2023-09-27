package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> user = new HashSet<>();

    public RoleEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public RoleEntity(String name) {
        this.name = name;
    }
}
