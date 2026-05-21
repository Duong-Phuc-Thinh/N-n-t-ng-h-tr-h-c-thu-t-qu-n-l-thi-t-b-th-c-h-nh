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

    @NotNull(message = "Vui lòng chọn khoa")
    private Long departmentId;

    @NotNull(message = "Vui lòng chọn ngành đào tạo")
    private Long majorId;

    @NotNull(message = "Vui lòng chọn giảng viên")
    private Long lecturerId;

    @NotNull(message = "Vui lòng chọn ngày tư vấn")
    @FutureOrPresent(message = "Ngày tư vấn không được ở trong quá khứ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;

    @NotNull(message = "Vui lòng chọn khung giờ")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeSlot;

    @NotBlank(message = "Vui lòng nhập chủ đề tư vấn")
    @Size(max = 150, message = "Chủ đề không được vượt quá 150 ký tự")
    private String topic;

    private String description;

    private Boolean needsEquipment = false;

    @Size(max = 150, message = "Ten thiet bi khong duoc vuot qua 150 ky tu")
    private String requestedEquipmentName;

    private Integer requestedEquipmentQuantity;

    @Size(max = 500, message = "Ghi chu thiet bi khong duoc vuot qua 500 ky tu")
    private String requestedEquipmentNote;
}
