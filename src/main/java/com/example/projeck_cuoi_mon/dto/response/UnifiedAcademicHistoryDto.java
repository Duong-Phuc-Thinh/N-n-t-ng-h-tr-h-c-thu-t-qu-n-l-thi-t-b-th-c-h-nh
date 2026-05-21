package com.example.projeck_cuoi_mon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedAcademicHistoryDto {
    private Long mentoringSessionId;
    private LocalDateTime scheduledAt;
    private String topic;
    private String description;
    private String lecturerName;
    private String departmentName;
    private String majorName;
    private String grade;
    private Integer score;
    private String comment;
    private List<BorrowedEquipmentDto> borrowedEquipments;
}
