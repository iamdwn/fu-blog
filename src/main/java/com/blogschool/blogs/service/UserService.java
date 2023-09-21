//package com.blogschool.blogs.service;
//
//import com.blogschool.blogs.dto.UserDTO;
//import com.blogschool.blogs.entity.UserEntity;
//import com.blogschool.blogs.exception.UserException;
//import com.blogschool.blogs.hash.Hashing;
//import com.blogschool.blogs.repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class UserService {
//
//    private Hashing hashing;
//    private UserRepository userRepository;
//
//    public UserEntity login(String username, String password) {
//        Optional<UserEntity> o_user = userRepository.findByUsername(username);
//        if (o_user.isEmpty()) {
//            throw new UserException("User is not found");
//        } else {
//            String storedPassword = o_user.get().getPassword();
//            String hashedPassword = hashing.hasdPassword(password);
//            boolean result = hashedPassword.equals(storedPassword);
//            if (result) {
//                return o_user.get();
//            } else throw new UserException("Password is incorrect");
//        }
//    }
//
//    public void signUpUser(UserDTO userDTO){
//        String hashedPassword = hashing.hasdPassword(userDTO.getPassword());
//        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()){
//            throw new UserException("Email is exist");
//        }
//        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()){
//            throw new UserException("Username is exist");
//        }
//        UserEntity user = new UserEntity(
//                userDTO.getUsername(),
//                hashedPassword,
//                userDTO.getEmail(),
//                userDTO.getFullName(),
//                true
//        );
//        userRepository.save(user);
//        userRepository.findByUsername(user.getUsername());
//
//    }
//}
