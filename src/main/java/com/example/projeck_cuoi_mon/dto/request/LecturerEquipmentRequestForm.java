package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class LecturerEquipmentRequestForm {

    @NotBlank(message = "Mục đích mượn không được để trống")
    @Size(max = 200, message = "Mục đích mượn không được vượt quá 200 ký tự")
    private String purpose;

    @NotNull(message = "Vui lòng chọn thiết bị")
    private Long equipmentId;

    @NotNull(message = "Vui lòng nhập số lượng")
    @Min(value = 1, message = "Số lượng mượn phải ít nhất là 1")
    private Integer quantity;

    @NotNull(message = "Vui lòng chọn ngày mượn")
    @FutureOrPresent(message = "Ngày mượn không được ở trong quá khứ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate borrowDate;

    @NotNull(message = "Vui lòng chọn ngày trả dự kiến")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedReturnDate;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String note;

    @AssertTrue(message = "Ngày trả dự kiến phải lớn hơn hoặc bằng ngày mượn")
    public boolean isReturnDateValid() {
        if (borrowDate == null || expectedReturnDate == null) {
            return true;
        }
        return !expectedReturnDate.isBefore(borrowDate);
    }
}
