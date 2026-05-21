package com.example.projeck_cuoi_mon.dto.response;

public record MajorView(
        Long id,
        Long departmentId,
        String departmentName,
        String code,
        String name,
        String description,
        Boolean active
) {
}
