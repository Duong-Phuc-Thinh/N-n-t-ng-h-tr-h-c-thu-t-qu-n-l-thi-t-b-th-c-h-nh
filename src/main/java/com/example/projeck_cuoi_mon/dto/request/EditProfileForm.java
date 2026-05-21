package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileForm {

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 120, message = "Họ và tên không quá 120 ký tự")
    private String fullName;

    @Size(max = 20, message = "Số điện thoại không quá 20 ký tự")
    private String phone;

    // Student fields
    private String className;

    // Lecturer fields
    private String academicTitle;
    private String specialization;
}
