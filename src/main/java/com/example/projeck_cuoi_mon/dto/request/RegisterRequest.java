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

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Size(max = 120, message = "Email must be at most 120 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be from 6 to 50 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name must be at most 120 characters")
    private String fullName;

    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @NotNull(message = "Role is required")
    private UserRole role;

    private Long departmentId;

    @Size(max = 30, message = "Student code must be at most 30 characters")
    private String studentCode;

    @Size(max = 50, message = "Class name must be at most 50 characters")
    private String className;

    @Size(max = 30, message = "Lecturer code must be at most 30 characters")
    private String lecturerCode;

    @Size(max = 80, message = "Academic title must be at most 80 characters")
    private String academicTitle;

    @Size(max = 150, message = "Specialty must be at most 150 characters")
    private String specialty;
}
