package tech.fublog.FuBlog.repository;

import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    List<VoteEntity> findByPostVote(BlogPostEntity blogPostEntity);

    VoteEntity findByUserVoteAndPostVote(UserEntity userEntity, BlogPostEntity blogPostEntity);
}
