package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.UserAwardEntity;

import java.util.Optional;

@Repository
public interface UserAwardRepository extends JpaRepository<UserAwardEntity, Long> {
//    Optional<UserAwardEntity> findByName(String awardName);

}
