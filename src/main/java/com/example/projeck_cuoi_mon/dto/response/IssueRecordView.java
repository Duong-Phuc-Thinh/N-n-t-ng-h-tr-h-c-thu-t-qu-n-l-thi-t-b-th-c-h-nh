package com.example.projeck_cuoi_mon.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class IssueRecordView {

    private Long id;
    private String studentName;
    private String purpose;
    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private List<IssueDetailView> details;
    private boolean issuable;
}
