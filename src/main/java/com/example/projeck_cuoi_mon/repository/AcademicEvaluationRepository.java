package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.AcademicEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicEvaluationRepository extends JpaRepository<AcademicEvaluation, Long> {

    List<AcademicEvaluation> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<AcademicEvaluation> findByLecturerIdOrderByCreatedAtDesc(Long lecturerId);

    Optional<AcademicEvaluation> findByMentoringSessionId(Long mentoringSessionId);
}
