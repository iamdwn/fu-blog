package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.FollowEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Long countByFollower(UserEntity userEntity);

    Long countByFollowing(UserEntity userEntity);

    List<FollowEntity> findByFollower(UserEntity userEntity);

    List<FollowEntity> findByFollowing(UserEntity userEntity);

    FollowEntity findByFollowerAndFollowing(UserEntity follower, UserEntity following);
}
