package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Users")
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


    @OneToMany(mappedBy = "authors")
    @JsonIgnore
    private List<BlogPostEntity> blogAuthors = new ArrayList<>();
//    @OneToMany(mappedBy = "authorsModified")
//    private Set<BlogPostEntity> blogAuthorsModified = new HashSet<>();

//    @OneToMany(mappedBy = "request")
//    private Set<ApprovalRequestEntity> requested = new HashSet<>();

    @OneToMany(mappedBy = "review")
    @JsonIgnore
    private List<ApprovalRequestEntity> reviewed = new ArrayList<>();

    @OneToMany(mappedBy = "userComment")
    @JsonIgnore
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "userVote")
    @JsonIgnore
    private List<VoteEntity> votes = new ArrayList<>();

    @OneToMany(mappedBy = "notification")
    @JsonIgnore
    private List<NotificationEntity> notificationList = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    @JsonIgnore
    private List<FollowEntity> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private List<FollowEntity> followersList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserAwardEntity> userAwards = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name = "Users_Id"),
    inverseJoinColumns = @JoinColumn(name = "Roles_Id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "bookmark",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<BlogPostEntity> blogPosts = new ArrayList<>();

    public UserEntity(String fullName, String username, String email, String hashedpassword, String picture, Boolean status) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.hashedpassword = hashedpassword;
        this.picture = picture;
        this.status = status;
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
}
