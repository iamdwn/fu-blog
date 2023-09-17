package com.blogschool.blogs.repository;

import com.blogschool.blogs.entity.ApprovalRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequestEntity, Long> {
}
