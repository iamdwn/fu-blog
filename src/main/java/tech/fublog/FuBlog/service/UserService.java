package tech.fublog.FuBlog.service;

import org.springframework.http.ResponseEntity;
import tech.fublog.FuBlog.dto.UserInfoDTO;
import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.model.ResponseObject;


public interface UserService {

    public UserEntity saveUser(UserEntity user);

    public RoleEntity saveRole(RoleEntity role);

    void addToUser(String username, String role);

    ResponseEntity<ResponseObject> getActiveUser();

    UserInfoDTO getUserInfo(Long userId);
}
