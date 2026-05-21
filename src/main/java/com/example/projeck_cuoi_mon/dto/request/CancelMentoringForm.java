package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelMentoringForm {

    @Size(max = 500, message = "Lý do hủy không được vượt quá 500 ký tự")
    private String reason;
}
