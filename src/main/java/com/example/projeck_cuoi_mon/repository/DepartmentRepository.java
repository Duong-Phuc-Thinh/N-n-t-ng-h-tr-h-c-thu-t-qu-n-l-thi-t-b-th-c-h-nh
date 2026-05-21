package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);

    long countByActiveTrue();

    @Query("select d from Department d order by d.name")
    List<Department> findAllOrderByName();
}
