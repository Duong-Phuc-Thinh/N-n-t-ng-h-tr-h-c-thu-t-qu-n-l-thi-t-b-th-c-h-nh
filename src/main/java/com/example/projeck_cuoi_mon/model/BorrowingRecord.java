package com.example.projeck_cuoi_mon.model;

import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrowing_records")
public class BorrowingRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_lecturer_id")
    private Lecturer approvedByLecturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exported_by_admin_id")
    private User exportedByAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoring_session_id")
    private MentoringSession mentoringSession;

    @Column(nullable = false, length = 200)
    private String purpose;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BorrowingStatus status = BorrowingStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "borrowingRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BorrowingDetail> details = new ArrayList<>();
}
