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

    @NotNull(message = "Danh mục phòng lab không được để trống")
    private Long labRoomTypeId;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(max = 150, message = "Tên thiết bị không được vượt quá 150 ký tự")
    private String name;

    private String description;

    @NotNull(message = "Tổng số lượng không được để trống")
    @Min(value = 0, message = "Tổng số lượng phải lớn hơn hoặc bằng 0")
    private Integer totalQuantity;

    @NotNull(message = "Số lượng khả dụng không được để trống")
    @Min(value = 0, message = "Số lượng khả dụng phải lớn hơn hoặc bằng 0")
    private Integer availableQuantity;

    @NotNull(message = "Trạng thái không được để trống")
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    @AssertTrue(message = "Số lượng khả dụng không được lớn hơn tổng số lượng")
    public boolean isQuantityValid() {
        if (totalQuantity == null || availableQuantity == null) {
            return true;
        }
        return availableQuantity <= totalQuantity;
    }
}
