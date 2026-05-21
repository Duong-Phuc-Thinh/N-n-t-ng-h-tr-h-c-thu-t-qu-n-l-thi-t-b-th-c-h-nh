package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.BorrowingRecord;
import com.example.projeck_cuoi_mon.dto.response.BorrowingHistoryDto;
import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    List<BorrowingRecord> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    @Query("""
            select distinct br from BorrowingRecord br
            left join fetch br.details d
            left join fetch d.equipment
            where br.student.id = :userId
            order by br.createdAt desc
            """)
    List<BorrowingRecord> findEquipmentRequestsByUserId(@Param("userId") Long userId);

    @Query("""
            select distinct br from BorrowingRecord br
            left join fetch br.details d
            left join fetch d.equipment
            left join fetch d.equipment.labRoomType
            where br.mentoringSession.id = :sessionId
            """)
    Optional<BorrowingRecord> findByMentoringSessionId(@Param("sessionId") Long sessionId);

    List<BorrowingRecord> findByStatusOrderByCreatedAtDesc(BorrowingStatus status);

    @Query("""
            select distinct br from BorrowingRecord br
            join fetch br.student
            left join fetch br.details d
            left join fetch d.equipment
            where br.status = :status
            order by br.createdAt desc
            """)
    List<BorrowingRecord> findIssueQueue(@Param("status") BorrowingStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select distinct br from BorrowingRecord br
            join fetch br.student
            left join fetch br.details d
            left join fetch d.equipment
            where br.id = :id
            """)
    Optional<BorrowingRecord> findByIdForIssue(@Param("id") Long id);

    @Query("""
            select new com.example.projeck_cuoi_mon.dto.response.BorrowingHistoryDto(
                br.id,
                br.borrowDate,
                br.expectedReturnDate,
                br.status,
                e.name,
                e.code,
                d.quantity
            )
            from BorrowingRecord br
            join br.details d
            join d.equipment e
            where br.student.id = :studentId
            order by br.borrowDate desc, br.id desc, e.name asc
            """)
    List<BorrowingHistoryDto> findStudentBorrowingHistory(@Param("studentId") Long studentId);

    @Query("""
            select new com.example.projeck_cuoi_mon.dto.response.BorrowingHistoryDto(
                br.id,
                br.borrowDate,
                br.expectedReturnDate,
                br.status,
                e.name,
                e.code,
                d.quantity
            )
            from BorrowingRecord br
            join br.details d
            join d.equipment e
            where br.student.id = :studentId
              and br.status not in :excludedStatuses
            order by br.expectedReturnDate asc, br.id desc, e.name asc
            """)
    List<BorrowingHistoryDto> findActiveStudentBorrowingHistory(
            @Param("studentId") Long studentId,
            @Param("excludedStatuses") List<BorrowingStatus> excludedStatuses
    );

    @Query("""
            select coalesce(sum(d.quantity), 0)
            from BorrowingRecord br
            join br.details d
            where br.student.id = :studentId
              and br.status not in :excludedStatuses
            """)
    long countStudentActiveBorrowedQuantity(
            @Param("studentId") Long studentId,
            @Param("excludedStatuses") List<BorrowingStatus> excludedStatuses
    );

    @Query("""
            select count(distinct br.id)
            from BorrowingRecord br
            where br.student.id = :studentId
              and br.expectedReturnDate < :today
              and br.status not in :excludedStatuses
            """)
    long countStudentOverdueRecords(
            @Param("studentId") Long studentId,
            @Param("today") LocalDate today,
            @Param("excludedStatuses") List<BorrowingStatus> excludedStatuses
    );

    @Query("""
            select count(distinct br.id)
            from BorrowingRecord br
            where br.approvedByLecturer.id = :lecturerId
              and br.status not in :excludedStatuses
            """)
    long countLecturerLinkedEquipmentRequests(
            @Param("lecturerId") Long lecturerId,
            @Param("excludedStatuses") List<BorrowingStatus> excludedStatuses
    );
}
