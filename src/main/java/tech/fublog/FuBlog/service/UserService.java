package tech.fublog.FuBlog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.response.UserInfoDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;
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

import java.time.ZoneId;
import java.util.*;


@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       BlogPostRepository blogPostRepository) {
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
    }


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Hashing hashing;

    private PasswordEncoder passwordEncoder;



    public UserEntity saveUser(UserEntity user) {
//        String pass = hashing.hasdPassword(user.getHashed_password());
//        user.setHashed_password(pass);
        user.setHashedpassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }



    public RoleEntity saveRole(RoleEntity role) {
        return null;
    }


    public void addToUser(String username, String rolename) {
        UserEntity user = userRepository.findByUsername(username).get();
        RoleEntity role  = roleRepository.findByName(rolename);
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


    public List<UserEntity> getAllUser(){
//        Pageable pageable = PageRequest.of(page,size);
        return  userRepository.findAll();
    }

    public boolean markPost(Long userId, Long postId) {
        boolean result = false;
//        Optional<UserEntity> userEntity = userRepository.findById(userId);
//        if (userEntity.isPresent()) {
//            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
//            if (blogPostEntity.isPresent()) {
//                Set<BlogPostEntity> entitySet;
//                if (userEntity.get().getMarkPosts().isEmpty()) {
//                    entitySet = new HashSet<>();
//                    entitySet.add(blogPostEntity.get());
//                    userEntity.get().setMarkPosts(entitySet);
//                    userRepository.save(userEntity.get());
//                    result = true;
//                } else {
//                    entitySet = userEntity.get().getMarkPosts();
//                    if (entitySet.add(blogPostEntity.get())) {
//                        userEntity.get().setMarkPosts(entitySet);
//                        userRepository.save(userEntity.get());
//                        result = true;
//                    } else throw new UserException("You already mark this post!");
//                }
//            }
//        }
        return result;
    }

    public UserEntity getUserById(Long userId) {

        return userRepository.findById(userId).orElse(null);
    }
    public ResponseEntity<ResponseObject> deleteBlogPost(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if (userEntity.isPresent()
                && userEntity.get().getStatus()) {
            UserEntity user = this.getUserById(userId);

            user.setStatus(false);

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("OK", "Deleted successful", user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("Not found", "Post not found", ""));
    }
    public ResponseEntity<ResponseObject> updateUser(Long userId, UserDTO userDTO) {

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if(userEntity.isPresent()){
            if(userDTO.getRole() != null) {
                Set<RoleEntity> roleEntities = new HashSet<>();

                RoleEntity userRole = roleRepository.findByName(userDTO.getRole().toUpperCase());

                roleEntities.add(userRole);

                UserEntity user = this.getUserById(userId);
                user.setFullName(userDTO.getFullname());
                user.setEmail(userDTO.getEmail());
                user.setPicture(userDTO.getPicture());
                user.setRoles(roleEntities);
                user.setStatus(userDTO.getStatus());
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "updated successful", user));
            }else{
                UserEntity user = this.getUserById(userId);
                user.setFullName(userDTO.getFullname());
                user.setEmail(userDTO.getEmail());
                user.setPicture(userDTO.getPicture());
                user.setStatus(userDTO.getStatus());
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "updated successful", user));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "updated failed", ""));
    }

}
