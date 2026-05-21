package com.example.projeck_cuoi_mon.dto.request;

import com.example.projeck_cuoi_mon.model.enums.EvaluationGrade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteMentoringForm {

    @NotNull(message = "Evaluation grade is required")
    private EvaluationGrade grade = EvaluationGrade.GOOD;

    @NotBlank(message = "Consultation content is required")
    @Size(min = 10, max = 2000, message = "Consultation content must be from 10 to 2000 characters")
    private String comment;

    private java.util.List<EquipmentLendItem> items = new java.util.ArrayList<>();
}
