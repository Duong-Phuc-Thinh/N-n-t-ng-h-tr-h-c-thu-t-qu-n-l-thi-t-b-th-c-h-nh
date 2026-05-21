package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.request.CompleteMentoringForm;
import com.example.projeck_cuoi_mon.dto.request.EditMentoringScheduleForm;
import com.example.projeck_cuoi_mon.dto.request.MentoringBookingForm;
import com.example.projeck_cuoi_mon.dto.response.BorrowedEquipmentDto;
import com.example.projeck_cuoi_mon.dto.response.MentoringSessionView;
import com.example.projeck_cuoi_mon.model.AcademicEvaluation;
import com.example.projeck_cuoi_mon.model.Lecturer;
import com.example.projeck_cuoi_mon.model.MentoringSession;
import com.example.projeck_cuoi_mon.model.Major;
import com.example.projeck_cuoi_mon.model.User;
import com.example.projeck_cuoi_mon.model.enums.EvaluationGrade;
import com.example.projeck_cuoi_mon.model.enums.MentoringStatus;
import com.example.projeck_cuoi_mon.model.BorrowingRecord;
import com.example.projeck_cuoi_mon.model.BorrowingDetail;
import com.example.projeck_cuoi_mon.model.Equipment;
import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import com.example.projeck_cuoi_mon.model.enums.BorrowingDetailStatus;
import com.example.projeck_cuoi_mon.repository.AcademicEvaluationRepository;
import com.example.projeck_cuoi_mon.repository.LecturerRepository;
import com.example.projeck_cuoi_mon.repository.MentoringSessionRepository;
import com.example.projeck_cuoi_mon.repository.MajorRepository;
import com.example.projeck_cuoi_mon.repository.UserRepository;
import com.example.projeck_cuoi_mon.repository.BorrowingRecordRepository;
import com.example.projeck_cuoi_mon.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MentoringService {

    private static final int SESSION_DURATION_MINUTES = 60;
    private static final int CANCEL_BEFORE_HOURS = 24;

    private final MentoringSessionRepository mentoringSessionRepository;
    private final LecturerRepository lecturerRepository;
    private final UserRepository userRepository;
    private final AcademicEvaluationRepository academicEvaluationRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final EquipmentRepository equipmentRepository;
    private final MajorRepository majorRepository;

    public List<LocalTime> getTimeSlots() {
        return List.of(
                LocalTime.of(8, 0),
                LocalTime.of(9, 30),
                LocalTime.of(13, 30),
                LocalTime.of(15, 0)
        );
    }

    @Transactional(readOnly = true)
    public List<MentoringSessionView> findStudentBookings(Long studentId) {
        return mentoringSessionRepository.findStudentHistory(studentId)
                .stream()
                .map(this::toView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MentoringSessionView> findUpcomingStudentBookings(Long studentId) {
        return mentoringSessionRepository.findUpcomingStudentSessions(
                        studentId,
                        LocalDateTime.now(),
                        List.of(MentoringStatus.PENDING, MentoringStatus.APPROVED)
                )
                .stream()
                .map(this::toView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MentoringSessionView> findLecturerBookings(Long lecturerUserId) {
        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer profile not found"));
        return mentoringSessionRepository.findLecturerHistory(lecturer.getId())
                .stream()
                .map(this::toView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MentoringSessionView> findLecturerDashboardBookings(Long lecturerId) {
        return mentoringSessionRepository.findLecturerDashboardSessions(
                        lecturerId,
                        List.of(MentoringStatus.PENDING, MentoringStatus.APPROVED)
                )
                .stream()
                .map(this::toView)
                .toList();
    }

    @Transactional(readOnly = true)
    public MentoringSessionView findLecturerBooking(Long lecturerUserId, Long sessionId) {
        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer profile not found"));
        MentoringSession session = mentoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Mentoring session not found"));
        if (!session.getLecturer().getId().equals(lecturer.getId())) {
            throw new IllegalArgumentException("You can only view your own mentoring sessions");
        }
        return toView(session);
    }

    @Transactional(readOnly = true)
    public EditMentoringScheduleForm getEditScheduleForm(Long lecturerUserId, Long sessionId) {
        MentoringSessionView view = findLecturerBooking(lecturerUserId, sessionId);
        EditMentoringScheduleForm form = new EditMentoringScheduleForm();
        form.setBookingDate(view.getScheduledAt().toLocalDate());
        form.setTimeSlot(view.getScheduledAt().toLocalTime());
        form.setLocation(view.getLocation());
        return form;
    }

    @Transactional
    public void updateSchedule(Long lecturerUserId, Long sessionId, EditMentoringScheduleForm form) {
        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer profile not found"));
        MentoringSession session = mentoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Mentoring session not found"));

        if (!session.getLecturer().getId().equals(lecturer.getId())) {
            throw new IllegalArgumentException("You can only edit your own mentoring sessions");
        }
        if (session.getStatus() == MentoringStatus.COMPLETED || session.getStatus() == MentoringStatus.CANCELLED) {
            throw new IllegalArgumentException("Completed or cancelled sessions cannot be rescheduled");
        }

        LocalDateTime scheduledAt = LocalDateTime.of(form.getBookingDate(), form.getTimeSlot());
        if (scheduledAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule mentoring session in the past");
        }
        boolean changedSlot = !session.getScheduledAt().equals(scheduledAt);
        if (changedSlot && mentoringSessionRepository.existsBookedSlot(lecturer.getId(), scheduledAt, List.of(MentoringStatus.CANCELLED, MentoringStatus.REJECTED))) {
            throw new IllegalArgumentException("This lecturer already has a booking in the selected time slot");
        }

        session.setScheduledAt(scheduledAt);
        session.setLocation(StringUtils.hasText(form.getLocation()) ? form.getLocation().trim() : "Lecturer office");
    }

    @Transactional
    public void complete(Long lecturerUserId, Long sessionId, CompleteMentoringForm form) {
        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer profile not found"));
        MentoringSession session = mentoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Mentoring session not found"));
        if (!session.getLecturer().getId().equals(lecturer.getId())) {
            throw new IllegalArgumentException("You can only complete your own mentoring sessions");
        }
        if (session.getStatus() == MentoringStatus.CANCELLED || session.getStatus() == MentoringStatus.REJECTED) {
            throw new IllegalArgumentException("Cancelled or rejected sessions cannot be completed");
        }
        if (session.getStatus() != MentoringStatus.APPROVED) {
            throw new IllegalArgumentException("Only accepted mentoring sessions can be evaluated");
        }

        List<com.example.projeck_cuoi_mon.dto.request.EquipmentLendItem> selectedItems = form.getItems() == null
                ? List.of()
                : form.getItems().stream()
                .filter(item -> Boolean.TRUE.equals(item.getSelected()) && item.getQuantity() != null && item.getQuantity() > 0)
                .toList();
        List<Equipment> selectedEquipments = new ArrayList<>();
        for (com.example.projeck_cuoi_mon.dto.request.EquipmentLendItem item : selectedItems) {
            Equipment equipment = equipmentRepository.findById(item.getEquipmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Equipment not found: " + item.getEquipmentId()));
            if (!Boolean.TRUE.equals(equipment.getActive()) || equipment.getStatus() != com.example.projeck_cuoi_mon.model.enums.EquipmentStatus.AVAILABLE) {
                throw new IllegalArgumentException("Equipment is not available: " + equipment.getName());
            }
            if (item.getQuantity() > equipment.getQuantityAvailable()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock for " + equipment.getName());
            }
            selectedEquipments.add(equipment);
        }

        AcademicEvaluation evaluation = academicEvaluationRepository.findByMentoringSessionId(session.getId())
                .orElseGet(() -> AcademicEvaluation.builder()
                        .student(session.getStudent())
                        .lecturer(lecturer)
                        .mentoringSession(session)
                        .build());
        evaluation.setGrade(form.getGrade());
        evaluation.setScore(toScore(form.getGrade()));
        evaluation.setComment(form.getComment().trim());
        academicEvaluationRepository.save(evaluation);

        session.setLecturerNote(form.getComment().trim());
        session.setStatus(MentoringStatus.COMPLETED);
        mentoringSessionRepository.save(session);

        // Process equipment lending if items are selected
        if (!selectedItems.isEmpty()) {
            List<BorrowingDetail> details = new ArrayList<>();
            BorrowingRecord record = BorrowingRecord.builder()
                    .student(session.getStudent())
                    .approvedByLecturer(lecturer)
                    .mentoringSession(session)
                    .purpose("Cấp phát thiết bị phục vụ học thuật theo cố vấn học tập ca: " + session.getTopic())
                    .borrowDate(LocalDate.now())
                    .expectedReturnDate(LocalDate.now().plusDays(14))
                    .status(BorrowingStatus.WAITING_ALLOCATION)
                    .note("Được chỉ định từ ca cố vấn ngày " + session.getScheduledAt().toLocalDate())
                    .details(details)
                    .build();

            for (int i = 0; i < selectedItems.size(); i++) {
                com.example.projeck_cuoi_mon.dto.request.EquipmentLendItem item = selectedItems.get(i);
                Equipment equipment = selectedEquipments.get(i);
                BorrowingDetail detail = BorrowingDetail.builder()
                        .borrowingRecord(record)
                        .equipment(equipment)
                        .quantity(item.getQuantity())
                        .returnedQuantity(0)
                        .status(BorrowingDetailStatus.BORROWING)
                        .build();
                details.add(detail);
            }

            borrowingRecordRepository.save(record);
        }
    }

    @Transactional
    public void accept(Long lecturerUserId, Long sessionId) {
        Lecturer lecturer = lecturerRepository.findByUserId(lecturerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer profile not found"));
        MentoringSession session = mentoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Mentoring session not found"));

        if (!session.getLecturer().getId().equals(lecturer.getId())) {
            throw new IllegalArgumentException("You can only accept your own mentoring sessions");
        }
        if (session.getStatus() != MentoringStatus.PENDING) {
            throw new IllegalArgumentException("Only pending mentoring sessions can be accepted");
        }

        session.setStatus(MentoringStatus.APPROVED);
    }

    @Transactional
    public void book(Long studentId, MentoringBookingForm form) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Lecturer lecturer = lecturerRepository.findById(form.getLecturerId())
                .orElseThrow(() -> new IllegalArgumentException("Lecturer not found"));
        Major major = majorRepository.findById(form.getMajorId())
                .orElseThrow(() -> new IllegalArgumentException("Major not found"));

        if (!lecturer.getDepartment().getId().equals(form.getDepartmentId())) {
            throw new IllegalArgumentException("Selected lecturer does not belong to selected department");
        }
        if (!major.getDepartment().getId().equals(form.getDepartmentId())) {
            throw new IllegalArgumentException("Selected major does not belong to selected department");
        }

        LocalDateTime scheduledAt = LocalDateTime.of(form.getBookingDate(), form.getTimeSlot());
        if (scheduledAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book a mentoring session in the past");
        }

        if (mentoringSessionRepository.existsBookedSlot(lecturer.getId(), scheduledAt, List.of(MentoringStatus.CANCELLED, MentoringStatus.REJECTED))) {
            throw new IllegalArgumentException("This lecturer already has a booking in the selected time slot");
        }

        MentoringSession session = MentoringSession.builder()
                .student(student)
                .lecturer(lecturer)
                .major(major)
                .topic(form.getTopic().trim())
                .description(form.getDescription())
                .scheduledAt(scheduledAt)
                .durationMinutes(SESSION_DURATION_MINUTES)
                .location("Lecturer office")
                .status(MentoringStatus.PENDING)
                .build();

        mentoringSessionRepository.save(session);
    }

    @Transactional
    public void cancel(Long studentId, Long sessionId, String reason) {
        MentoringSession session = mentoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Mentoring session not found"));

        if (!session.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("You can only cancel your own booking");
        }

        if (session.getStatus() != MentoringStatus.PENDING) {
            throw new IllegalArgumentException("Only pending bookings can be cancelled");
        }

        LocalDateTime latestCancelTime = session.getScheduledAt().minusHours(CANCEL_BEFORE_HOURS);
        if (LocalDateTime.now().isAfter(latestCancelTime)) {
            throw new IllegalArgumentException("Booking can only be cancelled at least 24 hours before the session");
        }

        session.setStatus(MentoringStatus.CANCELLED);
        session.setCancellationReason(StringUtils.hasText(reason) ? reason.trim() : null);
    }

    private MentoringSessionView toView(MentoringSession session) {
        return MentoringSessionView.builder()
                .id(session.getId())
                .studentName(session.getStudent().getFullName())
                .lecturerName(session.getLecturer().getUser().getFullName())
                .departmentName(session.getLecturer().getDepartment().getName())
                .majorName(session.getMajor() == null ? "Chua chon nganh" : session.getMajor().getName())
                .topic(session.getTopic())
                .description(session.getDescription())
                .location(session.getLocation())
                .scheduledAt(session.getScheduledAt())
                .durationMinutes(session.getDurationMinutes())
                .status(session.getStatus())
                .cancellable(isCancellable(session))
                .borrowedEquipments(findBorrowedEquipments(session.getId()))
                .build();
    }

    private List<BorrowedEquipmentDto> findBorrowedEquipments(Long sessionId) {
        return borrowingRecordRepository.findByMentoringSessionId(sessionId)
                .map(record -> record.getDetails().stream()
                        .map(detail -> BorrowedEquipmentDto.builder()
                                .equipmentName(detail.getEquipment().getName())
                                .equipmentCode(detail.getEquipment().getCode())
                                .labRoomTypeName(detail.getEquipment().getLabRoomType().getName())
                                .quantity(detail.getQuantity())
                                .status(record.getStatus().name())
                                .build())
                        .toList())
                .orElse(List.of());
    }

    private boolean isCancellable(MentoringSession session) {
        return session.getStatus() == MentoringStatus.PENDING
                && LocalDateTime.now().isBefore(session.getScheduledAt().minusHours(CANCEL_BEFORE_HOURS));
    }

    private int toScore(EvaluationGrade grade) {
        return switch (grade) {
            case EXCELLENT -> 95;
            case GOOD -> 85;
            case AVERAGE -> 70;
            case WEAK -> 50;
        };
    }
}
