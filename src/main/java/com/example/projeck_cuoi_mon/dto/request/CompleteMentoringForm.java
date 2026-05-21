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

    @NotNull(message = "Vui lòng chọn xếp loại đánh giá")
    private EvaluationGrade grade = EvaluationGrade.GOOD;

    @NotBlank(message = "Nội dung tư vấn không được để trống")
    @Size(min = 10, max = 2000, message = "Nội dung tư vấn phải từ 10 đến 2000 ký tự")
    private String comment;

    private java.util.List<EquipmentLendItem> items = new java.util.ArrayList<>();
}
