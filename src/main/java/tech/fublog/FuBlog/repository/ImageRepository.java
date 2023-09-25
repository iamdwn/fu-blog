package tech.fublog.FuBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.fublog.FuBlog.entity.ImageEntity;

import java.util.Optional;

// ImageRepository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    Optional<ImageEntity> findById(Long id);

}