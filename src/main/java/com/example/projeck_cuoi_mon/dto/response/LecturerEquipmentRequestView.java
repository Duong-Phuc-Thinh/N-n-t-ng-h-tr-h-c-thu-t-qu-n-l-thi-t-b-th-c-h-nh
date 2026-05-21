package com.example.projeck_cuoi_mon.dto.response;

import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class LecturerEquipmentRequestView {

    private Long id;
    private String purpose;
    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private BorrowingStatus status;
    private String statusLabel;
    private String statusCssClass;
    private String note;
    private List<IssueDetailView> details;
}
