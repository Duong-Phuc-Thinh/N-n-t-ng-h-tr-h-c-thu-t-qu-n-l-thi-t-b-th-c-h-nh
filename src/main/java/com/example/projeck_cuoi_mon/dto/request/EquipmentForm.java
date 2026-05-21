package com.example.projeck_cuoi_mon.dto.request;

import com.example.projeck_cuoi_mon.model.enums.EquipmentStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentForm {

    @NotNull(message = "Lab room type is required")
    private Long labRoomTypeId;

    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must be at most 150 characters")
    private String name;

    private String description;

    @NotNull(message = "Total quantity is required")
    @Min(value = 0, message = "Total quantity must be greater than or equal to 0")
    private Integer totalQuantity;

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Available quantity must be greater than or equal to 0")
    private Integer availableQuantity;

    @NotNull(message = "Status is required")
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    @AssertTrue(message = "Available quantity must not be greater than total quantity")
    public boolean isQuantityValid() {
        if (totalQuantity == null || availableQuantity == null) {
            return true;
        }
        return availableQuantity <= totalQuantity;
    }
}
