package com.example.projeck_cuoi_mon.dto.response;

import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BorrowingHistoryDto {

    private Long borrowingRecordId;
    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private BorrowingStatus borrowingStatus;
    private String equipmentName;
    private String equipmentCode;
    private Integer quantity;
}
