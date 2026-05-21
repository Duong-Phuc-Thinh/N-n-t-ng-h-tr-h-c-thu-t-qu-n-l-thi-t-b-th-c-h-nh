package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.MentoringSession;
import com.example.projeck_cuoi_mon.dto.response.AcademicMentoringHistoryDto;
import com.example.projeck_cuoi_mon.model.enums.MentoringStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MentoringSessionRepository extends JpaRepository<MentoringSession, Long> {

    List<MentoringSession> findByStudentIdOrderByScheduledAtDesc(Long studentId);

    @Query("""
            select ms from MentoringSession ms
            join fetch ms.lecturer l
            join fetch l.user
            join fetch l.department
            left join fetch ms.major
            where ms.student.id = :studentId
            order by ms.scheduledAt desc
            """)
    List<MentoringSession> findStudentHistory(@Param("studentId") Long studentId);

    List<MentoringSession> findByLecturerIdOrderByScheduledAtDesc(Long lecturerId);

    @Query("""
            select ms from MentoringSession ms
            join fetch ms.student student
            join fetch ms.lecturer lecturer
            join fetch lecturer.user lecturerUser
            join fetch lecturer.department
            left join fetch ms.major
            where lecturer.id = :lecturerId
            order by ms.scheduledAt desc
            """)
    List<MentoringSession> findLecturerHistory(@Param("lecturerId") Long lecturerId);

    List<MentoringSession> findByStatus(MentoringStatus status);

    long countByStudentIdAndStatus(Long studentId, MentoringStatus status);

    long countByLecturerIdAndStatus(Long lecturerId, MentoringStatus status);

    @Query("""
            select ms from MentoringSession ms
            join fetch ms.lecturer l
            join fetch l.user
            join fetch l.department
            left join fetch ms.major
            where ms.student.id = :studentId
              and ms.scheduledAt >= :now
              and ms.status in :statuses
            order by ms.scheduledAt asc
            """)
    List<MentoringSession> findUpcomingStudentSessions(
            @Param("studentId") Long studentId,
            @Param("now") LocalDateTime now,
            @Param("statuses") List<MentoringStatus> statuses
    );

    @Query("""
            select ms from MentoringSession ms
            join fetch ms.student student
            join fetch ms.lecturer lecturer
            join fetch lecturer.user lecturerUser
            join fetch lecturer.department
            left join fetch ms.major
            where lecturer.id = :lecturerId
              and ms.status in :statuses
            order by ms.scheduledAt asc
            """)
    List<MentoringSession> findLecturerDashboardSessions(
            @Param("lecturerId") Long lecturerId,
            @Param("statuses") List<MentoringStatus> statuses
    );

    @Query("""
            select new com.example.projeck_cuoi_mon.dto.response.AcademicMentoringHistoryDto(
                ms.id,
                ms.scheduledAt,
                lecturerUser.fullName,
                department.name,
                major.name,
                ms.status,
                ae.grade,
                ae.score,
                ae.comment
            )
            from MentoringSession ms
            join ms.lecturer lecturer
            join lecturer.user lecturerUser
            join lecturer.department department
            left join ms.major major
            left join AcademicEvaluation ae
                on ae.mentoringSession = ms
                and ae.student.id = :studentId
            where ms.student.id = :studentId
            order by ms.scheduledAt desc
            """)
    List<AcademicMentoringHistoryDto> findAcademicMentoringHistory(@Param("studentId") Long studentId);

    @Query("""
            select case when count(ms) > 0 then true else false end from MentoringSession ms
            where ms.lecturer.id = :lecturerId
              and ms.scheduledAt = :scheduledAt
              and ms.status not in :excludedStatuses
            """)
    boolean existsBookedSlot(
            @Param("lecturerId") Long lecturerId,
            @Param("scheduledAt") LocalDateTime scheduledAt,
            @Param("excludedStatuses") List<MentoringStatus> excludedStatuses
    );
}
