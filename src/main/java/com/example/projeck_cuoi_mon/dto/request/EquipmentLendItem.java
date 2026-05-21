package com.example.projeck_cuoi_mon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentLendItem {
    private Long equipmentId;
    private String name;
    private String labRoomTypeName;
    private Integer availableQuantity;
    private Integer quantity;
    private Boolean selected;
}
