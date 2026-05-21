package com.example.projeck_cuoi_mon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminDashboardStats {

    private long totalEquipments;
    private long lowStockEquipments;
    private long activeEquipments;
    private long inactiveEquipments;
}
