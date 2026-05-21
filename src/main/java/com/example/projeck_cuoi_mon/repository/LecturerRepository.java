package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

    Optional<Lecturer> findByUserId(Long userId);

    Optional<Lecturer> findByLecturerCode(String lecturerCode);

    List<Lecturer> findByDepartmentId(Long departmentId);

    long countByDepartmentId(Long departmentId);

    @Query("""
            select l from Lecturer l
            join fetch l.user
            where l.department.id = :departmentId
            order by l.user.fullName
            """)
    List<Lecturer> findByDepartmentIdWithUser(@Param("departmentId") Long departmentId);

    @Query("""
            select l from Lecturer l
            join fetch l.user
            join fetch l.department
            order by l.department.name, l.user.fullName
            """)
    List<Lecturer> findAllWithUserAndDepartment();
}
