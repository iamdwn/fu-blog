package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Category")
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name") // Ánh xạ cột trong cơ sở dữ liệu
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<BlogPostEntity> blogPosts = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parentCategoryId")
    private CategoryEntity parentCategory;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<UserEntity> user = new HashSet<>();

    public CategoryEntity(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CategoryEntity category = (CategoryEntity) obj;
        return Objects.equals(id, category.getId()) &&
                Objects.equals(name, category.getName());
    }
}
