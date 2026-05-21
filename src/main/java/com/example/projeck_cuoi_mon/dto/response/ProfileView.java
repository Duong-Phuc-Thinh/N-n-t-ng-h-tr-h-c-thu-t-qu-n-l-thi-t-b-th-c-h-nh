package com.example.projeck_cuoi_mon.dto.response;

import com.example.projeck_cuoi_mon.model.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileView {

    private String fullName;
    private String email;
    private String phone;
    private UserRole role;

    private String studentCode;
    private String className;

    private String lecturerCode;
    private String academicTitle;
    private String specialty;

    private String departmentName;
}
