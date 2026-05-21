package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueBorrowingForm {

    @NotNull(message = "Borrowing record is required")
    private Long borrowingRecordId;
}
