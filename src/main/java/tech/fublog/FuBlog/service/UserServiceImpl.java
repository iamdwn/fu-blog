package tech.fublog.FuBlog.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.fublog.FuBlog.dto.UserInfoDTO;
import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.hash.Hashing;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.RoleRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Hashing hashing;

    private PasswordEncoder passwordEncoder;


    @Override
    public UserEntity saveUser(UserEntity user) {
//        String pass = hashing.hasdPassword(user.getHashed_password());
//        user.setHashed_password(pass);
        user.setHashedpassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public RoleEntity saveRole(RoleEntity role) {
        return null;
    }

    @Override
    public void addToUser(String username, String rolename) {
        UserEntity user = userRepository.findByUsername(username).get();
        RoleEntity role  = roleRepository.findByName(rolename);
        user.getRoles().add(role);
    }


    @Override
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


    @Override
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
}
