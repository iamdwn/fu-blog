package tech.fublog.FuBlog.entity;

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
    private  Boolean status;

    @Column
    private  Boolean isVerify;

    @Column
    private Double point;


    @OneToMany(mappedBy = "authors")
    private Set<BlogPostEntity> blogAuthors = new HashSet<>();
//    @OneToMany(mappedBy = "authorsModified")
//    private Set<BlogPostEntity> blogAuthorsModified = new HashSet<>();

    @OneToMany(mappedBy = "request")
    private Set<ApprovalRequestEntity> requested = new HashSet<>();

    @OneToMany(mappedBy = "review")
    private Set<ApprovalRequestEntity> reviewed = new HashSet<>();

    @OneToMany(mappedBy = "userComment")
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "userVote")
    private Set<VoteEntity> votes = new HashSet<>();

    @OneToMany(mappedBy = "notification")
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

    public UserEntity(String fullName, String username, String email, String hashedpassword, Boolean status) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.hashedpassword = hashedpassword;
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
