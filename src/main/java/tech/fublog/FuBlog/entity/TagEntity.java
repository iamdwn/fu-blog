package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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
    @JsonIgnore
    private Set<PostTagEntity> postTags = new HashSet<>();

//    @ManyToMany
//    @JoinTable(name = "PostTag",
//            joinColumns = @JoinColumn(name = "tag_id"),
//            inverseJoinColumns = @JoinColumn(name = "post_id"))
//    private Set<BlogPostEntity> blogPosts = new HashSet<>();


}
