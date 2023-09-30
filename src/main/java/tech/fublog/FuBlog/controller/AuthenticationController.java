package tech.fublog.FuBlog.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.auth.AuthenticationReponse;
import tech.fublog.FuBlog.auth.AuthenticationRequest;
import tech.fublog.FuBlog.auth.MessageResponse;
import tech.fublog.FuBlog.auth.SignupRequest;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.repository.RoleRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.fublog.FuBlog.service.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
@CrossOrigin(origins = "*")
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/getAllUser")
    public List<UserEntity> getAllUser(){
        return  userService.getAllUser();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Optional<UserEntity> o_user = userRepository.findByUsername(authenticationRequest.getUsername());
        if(o_user.isPresent()){
            String encodedPasswordFromDatabase  = o_user.get().getPassword();
//            if (!userRepository.existsByUsername(authenticationRequest.getUsername()))
//                return ResponseEntity.badRequest().body(new MessageResponse("Error: User or password are incorect"));
//            else
                if(!passwordEncoder.matches(authenticationRequest.getPassword(),encodedPasswordFromDatabase)){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username or password is wrong!"));
            }else{
                return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
            }
        }else return ResponseEntity.badRequest().body(new MessageResponse("Error: Username or password is wrong!"));
//        String encodedPasswordFromDatabase  = getEncodedPasswordFromDatabase(authenticationRequest.getUsername());
        //        }else if((storedPassword != hashedPassword))
        //        if (!userRepository.existsByUsername(authenticationRequest.getUsername())) {
        //            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username or password is wrong!"));


    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        UserEntity user = new UserEntity(signUpRequest.getFullName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPicture(),
                true
        );

        Set<RoleEntity> roleEntities = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName("USER");
        roleEntities.add(userRole);
        user.setRoles(roleEntities);
        userRepository.save(user);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(signUpRequest.getUsername(), signUpRequest.getPassword());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("google")
    public ResponseEntity<?> loginGoogle(@Valid @RequestBody SignupRequest signUpRequest) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Optional<UserEntity> o_user = userRepository.findByUsername(signUpRequest.getEmail());
        if (o_user.isPresent()) {
            String encodedPasswordFromDatabase = o_user.get().getPassword();
            if (!passwordEncoder.matches(signUpRequest.getPassword(), encodedPasswordFromDatabase)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username or password is wrong!"));
            } else {
                AuthenticationRequest authenticationRequest = new AuthenticationRequest(signUpRequest.getEmail(), signUpRequest.getPassword());
                return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
            }
        }
        UserEntity user = new UserEntity(signUpRequest.getFullName(),
                signUpRequest.getEmail(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPicture(),
                true
        );
        Set<RoleEntity> roleEntities = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName("USER");
        roleEntities.add(userRole);
        user.setRoles(roleEntities);
        userRepository.save(user);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(signUpRequest.getEmail(), signUpRequest.getPassword());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
