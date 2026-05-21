package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.LabRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabRoomTypeRepository extends JpaRepository<LabRoomType, Long> {

    Optional<LabRoomType> findByCode(String code);
}
