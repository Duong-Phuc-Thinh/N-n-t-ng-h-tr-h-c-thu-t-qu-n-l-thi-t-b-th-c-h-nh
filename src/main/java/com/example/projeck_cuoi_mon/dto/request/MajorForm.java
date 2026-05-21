package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajorForm {

    @NotNull(message = "Khoa quản lý không được để trống")
    private Long departmentId;

    @NotBlank(message = "Mã ngành không được để trống")
    @Size(max = 30, message = "Mã ngành không được vượt quá 30 ký tự")
    private String code;

    @NotBlank(message = "Tên ngành không được để trống")
    @Size(max = 150, message = "Tên ngành không được vượt quá 150 ký tự")
    private String name;

    private String description;

    private Boolean active = true;
}
