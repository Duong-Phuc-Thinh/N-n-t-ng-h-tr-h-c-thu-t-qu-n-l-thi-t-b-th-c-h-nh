package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabRoomTypeForm {

    @NotBlank(message = "Mã danh mục phòng lab không được để trống")
    @Size(max = 30, message = "Mã danh mục phòng lab không được vượt quá 30 ký tự")
    private String code;

    @NotBlank(message = "Tên danh mục phòng lab không được để trống")
    @Size(max = 150, message = "Tên danh mục phòng lab không được vượt quá 150 ký tự")
    private String name;

    private String description;

    private Boolean active = true;
}
