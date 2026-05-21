package com.example.projeck_cuoi_mon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AcademicHistoryView {

    private List<AcademicMentoringHistoryDto> mentoringHistories;
    private List<BorrowingHistoryDto> borrowingHistories;
    private List<UnifiedAcademicHistoryDto> unifiedHistories;
}
