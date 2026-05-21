package com.example.projeck_cuoi_mon.dto.request;

import com.example.projeck_cuoi_mon.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 120, message = "Email không được vượt quá 120 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 50, message = "Mật khẩu phải từ 6 đến 50 ký tự")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 120, message = "Họ và tên không được vượt quá 120 ký tự")
    private String fullName;

    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    private String phone;

    @NotNull(message = "Vai trò không được để trống")
    private UserRole role;

    private Long departmentId;

    @Size(max = 30, message = "Mã số sinh viên không được vượt quá 30 ký tự")
    private String studentCode;

    @Size(max = 50, message = "Tên lớp không được vượt quá 50 ký tự")
    private String className;

    @Size(max = 30, message = "Mã giảng viên không được vượt quá 30 ký tự")
    private String lecturerCode;

    @Size(max = 80, message = "Học hàm/học vị không được vượt quá 80 ký tự")
    private String academicTitle;

    @Size(max = 150, message = "Chuyên môn không được vượt quá 150 ký tự")
    private String specialty;
}
