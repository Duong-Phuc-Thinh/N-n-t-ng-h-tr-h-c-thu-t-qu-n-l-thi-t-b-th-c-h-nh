package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentForm {

    @NotBlank(message = "Mã khoa không được để trống")
    @Size(max = 30, message = "Mã khoa không được vượt quá 30 ký tự")
    private String code;

    @NotBlank(message = "Tên khoa không được để trống")
    @Size(max = 150, message = "Tên khoa không được vượt quá 150 ký tự")
    private String name;

    private String description;

    private Boolean active = true;
}
