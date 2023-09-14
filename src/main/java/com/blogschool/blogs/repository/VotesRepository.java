package com.blogschool.blogs.repository;

import com.blogschool.blogs.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<VoteEntity, Long> {
}
