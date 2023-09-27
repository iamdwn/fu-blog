package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.auth.AuthenticationReponse;
import tech.fublog.FuBlog.auth.AuthenticationRequest;
import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
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
    private final JwtService jwtService;

    public AuthenticationReponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        UserEntity user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();
        String fullname = user.getFullName();
        String password = user.getHashedpassword();
        String email = user.getEmail();
        Long id = user.getId();
        String picture = user.getPicture();
        List<RoleEntity> role = null;
        if(user != null){
            role = roleCustomRepo.getRole(user);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<RoleEntity> set = new HashSet<>();
        role.stream().forEach(c -> set.add(new RoleEntity(c.getName())));
        user.setRoles(set);
        set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
        var jwtToken = jwtService.generateToken(user, authorities);
        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);
        AuthenticationReponse authenticationReponse = new AuthenticationReponse();
        authenticationReponse.setToken(jwtToken);
        authenticationReponse.setRefreshToken(jwtRefreshToken);
        authenticationReponse.setFullname(fullname);
        authenticationReponse.setPicture(picture);
        authenticationReponse.setEmail(email);
        authenticationReponse.setId(id);
        authenticationReponse.setPassword(password);

        return authenticationReponse;
//        return AuthenticationReponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();

    }


}
