package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.fublog.FuBlog.entity.AwardEntity;

import java.util.Optional;

@Repository
public interface AwardRepository extends JpaRepository<AwardEntity, Long> {
    Optional<AwardEntity> findByName(String awardName);

}
