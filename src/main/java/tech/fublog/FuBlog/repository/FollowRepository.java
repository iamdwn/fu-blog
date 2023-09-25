package tech.fublog.FuBlog.repository;


import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.fublog.FuBlog.entity.FollowEntity;
import tech.fublog.FuBlog.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.UserEntity;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    @Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.follower = :follower_id AND f.following = :following_id")
    Long findFollowerIdAndFollowingId(@Param("follower_id") UserEntity followerId,
                                         @Param("following_id") UserEntity followingId);


    @Transactional
    @Modifying
    @Query("DELETE FROM FollowEntity f WHERE f.follower = :follower_id AND f.following = :following_id")
    void deleteFollowerIdAndFollowingId(@Param("follower_id") UserEntity followerId,
                                          @Param("following_id") UserEntity followingId);


}

