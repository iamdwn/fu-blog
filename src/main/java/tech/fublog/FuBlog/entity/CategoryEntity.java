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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Category")
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String categoryName;

    @OneToMany(mappedBy = "category")
//    @JsonIgnore
    private Set<BlogPostEntity> blogPosts = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parentCategoryId")
    private CategoryEntity parentCategory;

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CategoryEntity category = (CategoryEntity) obj;
        return Objects.equals(Id, category.getId());
    }
}
