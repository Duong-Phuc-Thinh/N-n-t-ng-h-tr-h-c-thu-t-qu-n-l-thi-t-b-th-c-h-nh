package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.request.LecturerEquipmentRequestForm;
import com.example.projeck_cuoi_mon.dto.response.IssueDetailView;
import com.example.projeck_cuoi_mon.dto.response.LecturerEquipmentRequestView;
import com.example.projeck_cuoi_mon.model.BorrowingDetail;
import com.example.projeck_cuoi_mon.model.BorrowingRecord;
import com.example.projeck_cuoi_mon.model.Equipment;
import com.example.projeck_cuoi_mon.model.User;
import com.example.projeck_cuoi_mon.model.enums.BorrowingDetailStatus;
import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import com.example.projeck_cuoi_mon.repository.BorrowingRecordRepository;
import com.example.projeck_cuoi_mon.repository.EquipmentRepository;
import com.example.projeck_cuoi_mon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerEquipmentRequestService {

    private final EquipmentRepository equipmentRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Equipment> findRequestableEquipments() {
        return equipmentRepository.findByActiveTrueOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<LecturerEquipmentRequestView> findMyRequests(Long lecturerUserId) {
        return borrowingRecordRepository.findEquipmentRequestsByUserId(lecturerUserId)
                .stream()
                .map(this::toView)
                .toList();
    }

    @Transactional
    public void createRequest(Long lecturerUserId, LecturerEquipmentRequestForm form) {
        User lecturerUser = userRepository.findById(lecturerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer user not found"));
        Equipment equipment = equipmentRepository.findById(form.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));

        if (!Boolean.TRUE.equals(equipment.getActive())) {
            throw new IllegalArgumentException("Selected equipment is inactive");
        }

        BorrowingRecord record = BorrowingRecord.builder()
                .student(lecturerUser)
                .purpose(form.getPurpose().trim())
                .borrowDate(form.getBorrowDate())
                .expectedReturnDate(form.getExpectedReturnDate())
                .status(BorrowingStatus.WAITING_ALLOCATION)
                .note(form.getNote())
                .build();

        BorrowingDetail detail = BorrowingDetail.builder()
                .borrowingRecord(record)
                .equipment(equipment)
                .quantity(form.getQuantity())
                .returnedQuantity(0)
                .status(BorrowingDetailStatus.BORROWING)
                .build();

        record.getDetails().add(detail);
        borrowingRecordRepository.save(record);
    }

    private LecturerEquipmentRequestView toView(BorrowingRecord record) {
        return LecturerEquipmentRequestView.builder()
                .id(record.getId())
                .purpose(record.getPurpose())
                .borrowDate(record.getBorrowDate())
                .expectedReturnDate(record.getExpectedReturnDate())
                .status(record.getStatus())
                .statusLabel(getStatusLabel(record.getStatus()))
                .statusCssClass(getStatusCssClass(record.getStatus()))
                .note(record.getNote())
                .details(record.getDetails().stream()
                        .map(detail -> IssueDetailView.builder()
                                .equipmentName(detail.getEquipment().getName())
                                .equipmentCode(detail.getEquipment().getCode())
                                .requestedQuantity(detail.getQuantity())
                                .availableQuantity(detail.getEquipment().getQuantityAvailable())
                                .enoughStock(detail.getEquipment().getQuantityAvailable() >= detail.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    private String getStatusLabel(BorrowingStatus status) {
        return switch (status) {
            case WAITING_ALLOCATION -> "DANG CHO ADMIN";
            case ISSUED -> "DA CAP PHAT";
            case REJECTED -> "BI TU CHOI";
            case RETURNED -> "DA TRA";
            case CANCELLED -> "DA HUY";
            case OVERDUE -> "QUA HAN";
            default -> status.name();
        };
    }

    private String getStatusCssClass(BorrowingStatus status) {
        return switch (status) {
            case ISSUED, RETURNED -> "badge active-badge";
            case REJECTED, CANCELLED, OVERDUE -> "badge inactive-badge";
            default -> "badge status-badge";
        };
    }
}
