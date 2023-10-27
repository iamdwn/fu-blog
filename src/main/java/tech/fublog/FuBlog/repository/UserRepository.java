package tech.fublog.FuBlog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.fublog.FuBlog.entity.UserAwardEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameAndStatusTrue(String userName);

    Boolean existsByUsername(String username);

    Boolean existsByHashedpassword(String password);

    Boolean existsByEmail(String email);

    List<UserEntity> findAllByOrderByPointDesc();

    Page<UserEntity> findAllByStatusIsTrue(Pageable pageable);
    Page<UserEntity> findAllByStatusIsTrueOrderByPointDesc(Pageable pageable);

    @Query("SELECT user FROM UserEntity user JOIN UserAwardEntity userAward WHERE user.status = true AND user.status = true AND user.id = userAward.user.id AND userAward.award.name = :award ORDER BY user.point DESC")
    Page<UserEntity> findAllByStatusIsTrueAndUserAwardsOrderByPointDesc(String award, Pageable pageable);

    @Query("SELECT user FROM UserEntity user JOIN UserAwardEntity userAward WHERE user.status = true AND user.point >= :rankPointStart AND user.point < :rankPointEnd ORDER BY user.point DESC")
    Page<UserEntity> findAllByStatusIsTrueAndRankPointOrderByPointDesc(@Param("rankPointStart") Long rankPointStart,
                                                                       @Param("rankPointEnd") Long rankPointEnd,
                                                                       Pageable pageable);

    UserEntity findByIdAndStatusIsTrue(Long userId);
    List<UserEntity> findAllByStatusIsFalse();



}

