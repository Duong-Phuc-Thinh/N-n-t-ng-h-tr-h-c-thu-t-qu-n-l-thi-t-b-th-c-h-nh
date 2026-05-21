package com.example.projeck_cuoi_mon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LecturerDashboardView {

    private long pendingMentoringCount;
    private long approvedMentoringCount;
    private long completedMentoringCount;
    private long linkedEquipmentRequestCount;
    private List<MentoringSessionView> actionMentorings;
}
