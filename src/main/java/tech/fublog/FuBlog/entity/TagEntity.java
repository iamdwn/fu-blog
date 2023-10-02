package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;

    @Column
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private Set<PostTagEntity> postTags = new HashSet<>();

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
