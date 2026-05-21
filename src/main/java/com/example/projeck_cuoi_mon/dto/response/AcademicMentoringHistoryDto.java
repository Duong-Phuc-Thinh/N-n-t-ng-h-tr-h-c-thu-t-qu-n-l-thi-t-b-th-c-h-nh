package com.example.projeck_cuoi_mon.dto.response;

import com.example.projeck_cuoi_mon.model.enums.EvaluationGrade;
import com.example.projeck_cuoi_mon.model.enums.MentoringStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AcademicMentoringHistoryDto {

    private Long mentoringSessionId;
    private LocalDateTime scheduledAt;
    private String lecturerName;
    private String departmentName;
    private String majorName;
    private MentoringStatus mentoringStatus;
    private EvaluationGrade evaluationResult;
    private Integer score;
    private String evaluationComment;
}
