package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MentoringBookingForm {

    @NotNull(message = "Department is required")
    private Long departmentId;

    @NotNull(message = "Major is required")
    private Long majorId;

    @NotNull(message = "Lecturer is required")
    private Long lecturerId;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must not be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;

    @NotNull(message = "Time slot is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeSlot;

    @NotBlank(message = "Topic is required")
    @Size(max = 150, message = "Topic must be at most 150 characters")
    private String topic;

    private String description;
}
