package com.example.projeck_cuoi_mon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedEquipmentDto {
    private String equipmentName;
    private String equipmentCode;
    private String labRoomTypeName;
    private Integer quantity;
    private String status;
}
