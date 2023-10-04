package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tag")
@EntityListeners(AuditingEntityListener.class)
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String tagName;

    @OneToMany(mappedBy = "tag")
//    @JsonIgnore
    private List<PostTagEntity> postTags = new ArrayList<>();

    //    @ManyToMany
//    @JoinTable(name = "PostTag",
//            joinColumns = @JoinColumn(name = "tag_id"),
//            inverseJoinColumns = @JoinColumn(name = "post_id"))
//    private Set<BlogPostEntity> blogPosts = new HashSet<>();
    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TagEntity tag = (TagEntity) obj;
        return Objects.equals(Id, tag.getId());
    }

}
