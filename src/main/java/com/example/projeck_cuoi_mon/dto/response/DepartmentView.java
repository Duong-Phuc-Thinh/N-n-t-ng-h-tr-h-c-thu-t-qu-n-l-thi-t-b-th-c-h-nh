package com.example.projeck_cuoi_mon.dto.response;

import java.util.List;

public record DepartmentView(
        Long id,
        String code,
        String name,
        String description,
        Boolean active,
        String headLecturerName,
        long lecturerCount,
        long studentCount,
        long majorCount,
        List<MajorView> majors
) {
}
