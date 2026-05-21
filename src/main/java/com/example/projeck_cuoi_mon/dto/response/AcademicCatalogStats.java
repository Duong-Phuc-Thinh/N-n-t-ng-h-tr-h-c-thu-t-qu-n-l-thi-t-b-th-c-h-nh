package com.example.projeck_cuoi_mon.dto.response;

public record AcademicCatalogStats(
        long departmentCount,
        long activeDepartmentCount,
        long majorCount,
        long activeMajorCount,
        long lecturerCount,
        long studentProfileCount
) {
}
