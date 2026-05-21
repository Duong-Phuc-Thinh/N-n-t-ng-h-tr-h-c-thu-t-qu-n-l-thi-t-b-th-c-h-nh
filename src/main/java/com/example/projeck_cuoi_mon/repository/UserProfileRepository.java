package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByStudentCode(String studentCode);

    long countByDepartmentId(Long departmentId);
}
