package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueBorrowingForm {

    @NotNull(message = "Mã bản ghi mượn thiết bị không được để trống")
    private Long borrowingRecordId;
}
