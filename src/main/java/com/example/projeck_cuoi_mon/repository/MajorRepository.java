package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {

    Optional<Major> findByCode(String code);

    List<Major> findByDepartmentIdOrderByNameAsc(Long departmentId);

    long countByDepartmentId(Long departmentId);

    long countByActiveTrue();

    @Query("select m from Major m join fetch m.department order by m.department.name, m.name")
    List<Major> findAllWithDepartment();
}
