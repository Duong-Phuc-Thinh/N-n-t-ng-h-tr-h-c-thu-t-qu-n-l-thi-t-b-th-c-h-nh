package com.example.projeck_cuoi_mon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudentDashboardView {

    private long completedMentoringCount;
    private long pendingMentoringCount;
    private long approvedMentoringCount;
    private long upcomingMentoringCount;
    private long activeBorrowedQuantity;
    private long overdueBorrowingRecordCount;
    private List<MentoringSessionView> upcomingMentorings;
    private List<BorrowingHistoryDto> activeBorrowings;
}
