package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.response.BorrowingHistoryDto;
import com.example.projeck_cuoi_mon.dto.response.LecturerDashboardView;
import com.example.projeck_cuoi_mon.dto.response.MentoringSessionView;
import com.example.projeck_cuoi_mon.dto.response.StudentDashboardView;
import com.example.projeck_cuoi_mon.model.Lecturer;
import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import com.example.projeck_cuoi_mon.model.enums.MentoringStatus;
import com.example.projeck_cuoi_mon.repository.BorrowingRecordRepository;
import com.example.projeck_cuoi_mon.repository.LecturerRepository;
import com.example.projeck_cuoi_mon.repository.MentoringSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final List<BorrowingStatus> CLOSED_BORROWING_STATUSES = List.of(
            BorrowingStatus.RETURNED,
            BorrowingStatus.CANCELLED,
            BorrowingStatus.REJECTED
    );

    private final MentoringSessionRepository mentoringSessionRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final LecturerRepository lecturerRepository;
    private final MentoringService mentoringService;

    @Transactional(readOnly = true)
    public StudentDashboardView getStudentDashboard(Long studentId) {
        List<MentoringSessionView> upcomingMentorings = mentoringService.findUpcomingStudentBookings(studentId);
        List<BorrowingHistoryDto> activeBorrowings = borrowingRecordRepository.findActiveStudentBorrowingHistory(
                studentId,
                CLOSED_BORROWING_STATUSES
        );

        return new StudentDashboardView(
                mentoringSessionRepository.countByStudentIdAndStatus(studentId, MentoringStatus.COMPLETED),
                mentoringSessionRepository.countByStudentIdAndStatus(studentId, MentoringStatus.PENDING),
                mentoringSessionRepository.countByStudentIdAndStatus(studentId, MentoringStatus.APPROVED),
                upcomingMentorings.size(),
                borrowingRecordRepository.countStudentActiveBorrowedQuantity(studentId, CLOSED_BORROWING_STATUSES),
                borrowingRecordRepository.countStudentOverdueRecords(studentId, LocalDate.now(), CLOSED_BORROWING_STATUSES),
                upcomingMentorings,
                activeBorrowings
        );
    }

    @Transactional(readOnly = true)
    public LecturerDashboardView getLecturerDashboard(Long lecturerUserId) {
        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer profile not found"));
        List<MentoringSessionView> actionMentorings = mentoringService.findLecturerDashboardBookings(lecturer.getId());

        return new LecturerDashboardView(
                mentoringSessionRepository.countByLecturerIdAndStatus(lecturer.getId(), MentoringStatus.PENDING),
                mentoringSessionRepository.countByLecturerIdAndStatus(lecturer.getId(), MentoringStatus.APPROVED),
                mentoringSessionRepository.countByLecturerIdAndStatus(lecturer.getId(), MentoringStatus.COMPLETED),
                borrowingRecordRepository.countLecturerLinkedEquipmentRequests(lecturer.getId(), CLOSED_BORROWING_STATUSES),
                actionMentorings
        );
    }
}
