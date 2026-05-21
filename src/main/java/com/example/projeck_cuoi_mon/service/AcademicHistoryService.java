package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.response.AcademicHistoryView;
import com.example.projeck_cuoi_mon.dto.response.BorrowedEquipmentDto;
import com.example.projeck_cuoi_mon.dto.response.UnifiedAcademicHistoryDto;
import com.example.projeck_cuoi_mon.model.AcademicEvaluation;
import com.example.projeck_cuoi_mon.model.BorrowingRecord;
import com.example.projeck_cuoi_mon.model.MentoringSession;
import com.example.projeck_cuoi_mon.repository.AcademicEvaluationRepository;
import com.example.projeck_cuoi_mon.repository.BorrowingRecordRepository;
import com.example.projeck_cuoi_mon.repository.MentoringSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicHistoryService {

    private final MentoringSessionRepository mentoringSessionRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final AcademicEvaluationRepository academicEvaluationRepository;

    @Transactional(readOnly = true)
    public AcademicHistoryView getStudentAcademicHistory(Long studentId) {
        List<MentoringSession> sessions = mentoringSessionRepository.findStudentHistory(studentId);
        List<UnifiedAcademicHistoryDto> unifiedHistories = new ArrayList<>();
        
        for (MentoringSession session : sessions) {
            // Find academic evaluation
            AcademicEvaluation evaluation = academicEvaluationRepository.findByMentoringSessionId(session.getId())
                    .orElse(null);
            
            // Find borrowing record
            BorrowingRecord borrowingRecord = borrowingRecordRepository.findByMentoringSessionId(session.getId())
                    .orElse(null);
            
            List<BorrowedEquipmentDto> borrowedEquipments = new ArrayList<>();
            if (borrowingRecord != null && borrowingRecord.getDetails() != null) {
                borrowedEquipments = borrowingRecord.getDetails().stream()
                        .map(detail -> BorrowedEquipmentDto.builder()
                                .equipmentName(detail.getEquipment().getName())
                                .equipmentCode(detail.getEquipment().getCode())
                                .labRoomTypeName(detail.getEquipment().getLabRoomType().getName())
                                .quantity(detail.getQuantity())
                                .status(borrowingRecord.getStatus().name())
                                .build())
                        .collect(Collectors.toList());
            }
            
            String grade = evaluation != null ? evaluation.getGrade().name() : null;
            Integer score = evaluation != null ? evaluation.getScore() : null;
            String comment = evaluation != null ? evaluation.getComment() : null;
            
            UnifiedAcademicHistoryDto dto = UnifiedAcademicHistoryDto.builder()
                    .mentoringSessionId(session.getId())
                    .scheduledAt(session.getScheduledAt())
                    .topic(session.getTopic())
                    .description(session.getDescription())
                    .lecturerName(session.getLecturer().getUser().getFullName())
                    .departmentName(session.getLecturer().getDepartment().getName())
                    .majorName(session.getMajor() == null ? "Chua chon nganh" : session.getMajor().getName())
                    .grade(grade)
                    .score(score)
                    .comment(comment)
                    .borrowedEquipments(borrowedEquipments)
                    .build();
            
            unifiedHistories.add(dto);
        }
        
        return new AcademicHistoryView(
                mentoringSessionRepository.findAcademicMentoringHistory(studentId),
                borrowingRecordRepository.findStudentBorrowingHistory(studentId),
                unifiedHistories
        );
    }
}
