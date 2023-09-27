package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.UserInfoDTO;
import tech.fublog.FuBlog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String userName);

    Boolean existsByUsername(String username);

    Boolean existsByHashedpassword(String password);

    Boolean existsByEmail(String email);

    List<UserEntity> findAllByOrderByPointDesc();


}

