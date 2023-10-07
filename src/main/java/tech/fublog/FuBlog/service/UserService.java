package tech.fublog.FuBlog.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.fublog.FuBlog.dto.UserInfoDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.UserException;
import tech.fublog.FuBlog.hash.Hashing;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.RoleRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final RoleRepository roleRepository;
    private final Hashing hashing;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       BlogPostRepository blogPostRepository, RoleRepository roleRepository, Hashing hashing, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
        this.roleRepository = roleRepository;
        this.hashing = hashing;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity saveUser(UserEntity user) {
        user.setHashedpassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public RoleEntity saveRole(RoleEntity role) {
        return null;
    }

    public void addToUser(String username, String rolename) {
        UserEntity user = userRepository.findByUsername(username).get();
        RoleEntity role = roleRepository.findByName(rolename);
        user.getRoles().add(role);
    }

    public ResponseEntity<ResponseObject> getActiveUser() {
        List<UserEntity> userEntities = userRepository.findAllByOrderByPointDesc();
        List<UserInfoDTO> highestPointUser = new ArrayList<>();
        for (UserEntity user : userEntities) {
            if (user.getPoint().equals(userEntities.get(0).getPoint())) {
//                UserDTO userDTO = new UserDTO(
//                        user.getUsername(),
//                        user.getFullName(),
//                        user.getEmail());
                UserInfoDTO userInfoDTO =
                        new UserInfoDTO(user.getUsername(), user.getPicture(), user.getPoint());
                highestPointUser.add(userInfoDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new tech.fublog.FuBlog.model.ResponseObject("found", "list found",
                        highestPointUser));
    }

    public UserInfoDTO getUserInfo(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return new UserInfoDTO(user.getUsername(), user.getPicture(), user.getPoint());
        }
        return null;
    }


    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    public String markPost(Long userId, Long postId) {
        String result = "Failed";
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
            if (blogPostEntity.isPresent()) {
                Set<BlogPostEntity> entitySet;
                if (userEntity.get().getMarkPosts().isEmpty()) {
                    entitySet = new HashSet<>();
                    entitySet.add(blogPostEntity.get());
                    userEntity.get().setMarkPosts(entitySet);
                    userRepository.save(userEntity.get());
                    return result = "Successfully";
                } else {
                    entitySet = userEntity.get().getMarkPosts();
                    if (entitySet.add(blogPostEntity.get())) {
                        userEntity.get().setMarkPosts(entitySet);
                        userRepository.save(userEntity.get());
                        return result = "Successfully";
                    } else throw new UserException("You already mark this post!");
                }
            } else throw new UserException("Blog doesn't exists!");
        } else throw new UserException("User doesn't exists");
    }

    public String unMarkPost(Long userId, Long postId) {
        String result = "Failed";
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
            if (blogPostEntity.isPresent()) {
                Set<BlogPostEntity> entitySet;
                if (userEntity.get().getMarkPosts().isEmpty()) {
                    return result = "Successfully";
                } else {
                    entitySet = userEntity.get().getMarkPosts();
                    for (BlogPostEntity entity : entitySet) {
                        if (entity.getId().equals(postId)) {
                            System.out.println(entity.getId());
                            entitySet.remove(entity);
                        }
                    }
                    userEntity.get().setMarkPosts(entitySet);
                    userRepository.save(userEntity.get());
                    return result = "Successfully";
                }
            } else throw new UserException("Blog doesn't exists!");
        } else throw new UserException("User doesn't exists");
    }

}
