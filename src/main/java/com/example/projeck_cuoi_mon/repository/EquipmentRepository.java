package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.Equipment;
import com.example.projeck_cuoi_mon.model.enums.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Optional<Equipment> findByCode(String code);

    @Query("select e from Equipment e join fetch e.labRoomType order by e.createdAt desc")
    List<Equipment> findAllWithLabRoomType();

    List<Equipment> findByStatus(EquipmentStatus status);

    List<Equipment> findByLabRoomTypeId(Long labRoomTypeId);

    @Query("select e from Equipment e join fetch e.labRoomType where e.labRoomType.id = :labRoomTypeId order by e.createdAt desc")
    List<Equipment> findByLabRoomTypeIdWithLabRoomType(@Param("labRoomTypeId") Long labRoomTypeId);

    long countByLabRoomTypeId(Long labRoomTypeId);

    long countByLabRoomTypeIdAndActiveTrue(Long labRoomTypeId);

    long countByLabRoomTypeIdAndStatus(Long labRoomTypeId, EquipmentStatus status);

    @Query("select coalesce(sum(e.quantityTotal), 0) from Equipment e where e.labRoomType.id = :labRoomTypeId")
    long sumQuantityTotalByLabRoomTypeId(@Param("labRoomTypeId") Long labRoomTypeId);

    @Query("select coalesce(sum(e.quantityAvailable), 0) from Equipment e where e.labRoomType.id = :labRoomTypeId")
    long sumQuantityAvailableByLabRoomTypeId(@Param("labRoomTypeId") Long labRoomTypeId);

    List<Equipment> findByActiveTrueOrderByNameAsc();

    @Query("select e from Equipment e join fetch e.labRoomType where e.active = true order by e.labRoomType.name, e.name")
    List<Equipment> findActiveWithLabRoomType();

    long countByActiveTrue();

    long countByActiveFalse();

    @Query("select count(e) from Equipment e where e.active = true and e.quantityAvailable <= :threshold")
    long countLowStock(@Param("threshold") int threshold);
}
