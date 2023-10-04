package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String hashedpassword;

    @Column
    private String picture;

    @Column
    private  Boolean status;

    @Column
    private  Boolean isVerify;

    @Column
    private Double point;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "user_mark_post",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<BlogPostEntity> markPosts = new HashSet<>();


    @OneToMany(mappedBy = "authors")
//    @JsonIgnore
    private Set<BlogPostEntity> blogAuthors = new HashSet<>();
//    @OneToMany(mappedBy = "authorsModified")
//    private Set<BlogPostEntity> blogAuthorsModified = new HashSet<>();

//    @OneToMany(mappedBy = "request")
//    private Set<ApprovalRequestEntity> requested = new HashSet<>();

    @OneToMany(mappedBy = "review")
    private Set<ApprovalRequestEntity> reviewed = new HashSet<>();

    @OneToMany(mappedBy = "userComment")
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "userVote")
    private Set<VoteEntity> votes = new HashSet<>();

    @OneToMany(mappedBy = "userId")
    private Set<NotificationEntity> notificationList = new HashSet<>();

    @OneToMany(mappedBy = "following")
    private Set<FollowEntity> followingList = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    private Set<FollowEntity> followersList = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserAwardEntity> userAwards = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "Users_Id"),
            inverseJoinColumns = @JoinColumn(name = "Roles_Id"))
    private Set<RoleEntity> roles = new HashSet<>();

    public UserEntity(String fullName, String username, String email, String hashedpassword, String picture, Boolean status,Boolean isVerify) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.hashedpassword = hashedpassword;
        this.picture = picture;
        this.status = status;
        this.isVerify = isVerify;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(i ->authorities.add(new SimpleGrantedAuthority(i.getName())));
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }




    public UserEntity(Long id, String fullName, String username, String email, String hashedpassword, Set<RoleEntity> roles) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.hashedpassword = hashedpassword;
        this.roles = roles;
    }

    @Override
    public String getPassword() {
        return hashedpassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserEntity user = (UserEntity) obj;
        return Objects.equals(id, user.getId()) &&
                Objects.equals(username, user.getUsername());
    }
}
