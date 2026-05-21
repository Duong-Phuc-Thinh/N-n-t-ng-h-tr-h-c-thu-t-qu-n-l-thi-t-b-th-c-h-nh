package com.example.projeck_cuoi_mon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lecturers")
public class Lecturer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "lecturer_code", nullable = false, unique = true, length = 30)
    private String lecturerCode;

    @Column(name = "academic_title", length = 80)
    private String academicTitle;

    @Column(length = 150)
    private String specialization;

    @Column(name = "office_room", length = 50)
    private String officeRoom;

    @OneToMany(mappedBy = "lecturer")
    @Builder.Default
    private List<MentoringSession> mentoringSessions = new ArrayList<>();

    @OneToMany(mappedBy = "lecturer")
    @Builder.Default
    private List<AcademicEvaluation> academicEvaluations = new ArrayList<>();
}
