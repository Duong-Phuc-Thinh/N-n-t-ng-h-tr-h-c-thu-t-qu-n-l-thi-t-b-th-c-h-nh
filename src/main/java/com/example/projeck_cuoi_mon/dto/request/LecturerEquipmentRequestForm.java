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

    @NotBlank(message = "Purpose is required")
    @Size(max = 200, message = "Purpose must be at most 200 characters")
    private String purpose;

    @NotNull(message = "Equipment is required")
    private Long equipmentId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Borrow date is required")
    @FutureOrPresent(message = "Borrow date must not be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate borrowDate;

    @NotNull(message = "Expected return date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedReturnDate;

    @Size(max = 500, message = "Note must be at most 500 characters")
    private String note;

    @AssertTrue(message = "Expected return date must be after or equal to borrow date")
    public boolean isReturnDateValid() {
        if (borrowDate == null || expectedReturnDate == null) {
            return true;
        }
        return !expectedReturnDate.isBefore(borrowDate);
    }
}
