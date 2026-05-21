package com.example.projeck_cuoi_mon.dto.response;

public record LabRoomTypeView(
        Long id,
        String code,
        String name,
        String description,
        Boolean active,
        long equipmentCount,
        long activeEquipmentCount,
        long maintenanceEquipmentCount,
        long totalQuantity,
        long availableQuantity
) {
}
