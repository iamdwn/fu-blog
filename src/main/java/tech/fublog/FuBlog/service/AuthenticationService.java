package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.auth.AuthenticationResponse;
import tech.fublog.FuBlog.auth.AuthenticationRequest;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.repository.CategoryCustomRepo;
import tech.fublog.FuBlog.repository.RoleCustomRepo;
import tech.fublog.FuBlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final RoleCustomRepo roleCustomRepo;
    private final CategoryCustomRepo categoryCustomRepo;
    private final JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        UserEntity user = userRepository.findByUsernameAndStatusTrue(authenticationRequest.getUsername()).orElseThrow();
        String fullname = user.getFullName();
        String password = user.getHashedpassword();
        String email = user.getEmail();
        Long id = user.getId();
        String picture = user.getPicture();
        List<RoleEntity> role = null;
        List<CategoryEntity> category = null;
        if(user != null){
            role = roleCustomRepo.getRole(user);
            category = categoryCustomRepo.getCategory(user);
        }


        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<RoleEntity> setRole = new HashSet<>();
        role.stream().forEach(r -> setRole.add(new RoleEntity(r.getName())));
//        System.out.println(setRole);
        Set<CategoryEntity> setCategory = new HashSet<>();
        category.stream().forEach(c -> setCategory.add(new CategoryEntity((c.getName()))));
//        System.out.println(category);
//        System.out.println(setCategory);
        user.setCategories(setCategory);
        user.setRoles(setRole);
        setRole.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
        setCategory.stream().forEach(a -> authorities.add(new SimpleGrantedAuthority(a.getName())));
        var jwtToken = jwtService.generateToken(user, authorities);
        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

        return AuthenticationResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();

    }
//        AuthenticationReponse authenticationReponse = new AuthenticationReponse();
//        authenticationReponse.setToken(jwtToken);
//        authenticationReponse.setRefreshToken(jwtRefreshToken);
//        authenticationReponse.setFullname(fullname);
//        authenticationReponse.setPicture(picture);
//        authenticationReponse.setEmail(email);
//        authenticationReponse.setId(id);
//        authenticationReponse.setPassword(password);
//        return authenticationReponse;

}
