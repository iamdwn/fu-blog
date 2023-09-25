package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;


public interface UserService {

    public UserEntity saveUser(UserEntity user);

    public RoleEntity saveRole(RoleEntity role);

    void addToUser(String username, String role);
}
