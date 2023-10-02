package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> user = new HashSet<>();

    //    @OneToMany(mappedBy = "role")
//    private Set<UserRoleEntity> userRoles = new HashSet<>();
    @Override
    public int hashCode() {
        return Objects.hash(Id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RoleEntity role = (RoleEntity) obj;
        return Objects.equals(Id, role.getId()) &&
                Objects.equals(name, role.getName());
    }
}
