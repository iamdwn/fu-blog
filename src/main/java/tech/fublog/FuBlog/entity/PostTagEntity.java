package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PostTag")
@EntityListeners(AuditingEntityListener.class)
public class PostTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BlogPostEntity post;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagEntity tag;

    public PostTagEntity(BlogPostEntity post, TagEntity tag) {
        this.post = post;
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PostTagEntity postTag = (PostTagEntity) obj;
        return Objects.equals(post, postTag.getPost()) &&
                Objects.equals(tag, postTag.getTag());
    }
}
