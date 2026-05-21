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

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must not be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;

    @NotNull(message = "Time slot is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeSlot;

    @Size(max = 120, message = "Location must be at most 120 characters")
    private String location;
}
