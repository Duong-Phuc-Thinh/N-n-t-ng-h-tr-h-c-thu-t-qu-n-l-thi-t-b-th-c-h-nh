package com.example.projeck_cuoi_mon.model;

import com.example.projeck_cuoi_mon.model.enums.UserRole;
import com.example.projeck_cuoi_mon.model.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @OneToOne(mappedBy = "user")
    private UserProfile profile;

    @OneToOne(mappedBy = "user")
    private Lecturer lecturer;

    @OneToMany(mappedBy = "student")
    @Builder.Default
    private List<MentoringSession> mentoringSessions = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    @Builder.Default
    private List<AcademicEvaluation> academicEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    @Builder.Default
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();
}
