package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EditMentoringScheduleForm {

    @NotNull(message = "Ngày tư vấn không được để trống")
    @FutureOrPresent(message = "Ngày tư vấn không được ở trong quá khứ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;

    @NotNull(message = "Khung giờ không được để trống")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeSlot;

    @Size(max = 120, message = "Địa điểm không được vượt quá 120 ký tự")
    private String location;
}
