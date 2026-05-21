package com.example.projeck_cuoi_mon.dto.response;

import com.example.projeck_cuoi_mon.model.enums.MentoringStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MentoringSessionView {

    private Long id;
    private String studentName;
    private String lecturerName;
    private String departmentName;
    private String majorName;
    private String topic;
    private String description;
    private String location;
    private LocalDateTime scheduledAt;
    private LocalDateTime cancellationDeadline;
    private Integer durationMinutes;
    private MentoringStatus status;
    private boolean cancellable;
    private List<BorrowedEquipmentDto> borrowedEquipments;
    private String requestedEquipmentName;
    private Integer requestedEquipmentQuantity;
    private String requestedEquipmentNote;
    private boolean hasEquipmentRequest;
}
