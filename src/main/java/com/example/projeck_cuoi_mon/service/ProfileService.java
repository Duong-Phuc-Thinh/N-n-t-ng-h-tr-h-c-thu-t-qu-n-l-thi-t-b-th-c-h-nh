package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.response.ProfileView;
import com.example.projeck_cuoi_mon.model.Lecturer;
import com.example.projeck_cuoi_mon.model.User;
import com.example.projeck_cuoi_mon.model.UserProfile;
import com.example.projeck_cuoi_mon.model.enums.UserRole;
import com.example.projeck_cuoi_mon.repository.LecturerRepository;
import com.example.projeck_cuoi_mon.repository.UserProfileRepository;
import com.example.projeck_cuoi_mon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final LecturerRepository lecturerRepository;

    @Transactional(readOnly = true)
    public ProfileView getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ProfileView.ProfileViewBuilder builder = ProfileView.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole());

        if (user.getRole() == UserRole.STUDENT) {
            userProfileRepository.findByUserId(user.getId())
                    .ifPresent(profile -> fillStudentProfile(builder, profile));
        }

        if (user.getRole() == UserRole.LECTURER) {
            lecturerRepository.findByUserId(user.getId())
                    .ifPresent(lecturer -> fillLecturerProfile(builder, lecturer));
        }

        return builder.build();
    }

    private void fillStudentProfile(ProfileView.ProfileViewBuilder builder, UserProfile profile) {
        builder.studentCode(profile.getStudentCode())
                .className(profile.getClassName())
                .departmentName(profile.getDepartment() == null ? null : profile.getDepartment().getName());
    }

    private void fillLecturerProfile(ProfileView.ProfileViewBuilder builder, Lecturer lecturer) {
        builder.lecturerCode(lecturer.getLecturerCode())
                .academicTitle(lecturer.getAcademicTitle())
                .specialty(lecturer.getSpecialization())
                .departmentName(lecturer.getDepartment().getName());
    }

    @Transactional
    public void updateProfile(Long userId, com.example.projeck_cuoi_mon.dto.request.EditProfileForm form) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(form.getFullName().trim());
        user.setPhone(form.getPhone() != null ? form.getPhone().trim() : null);

        if (user.getRole() == UserRole.STUDENT) {
            UserProfile profile = userProfileRepository.findByUserId(user.getId())
                    .orElseGet(() -> UserProfile.builder().user(user).build());
            profile.setClassName(form.getClassName() != null ? form.getClassName().trim() : null);
            userProfileRepository.save(profile);
        }

        if (user.getRole() == UserRole.LECTURER) {
            Lecturer lecturer = lecturerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Lecturer profile not found"));
            lecturer.setAcademicTitle(form.getAcademicTitle() != null ? form.getAcademicTitle().trim() : null);
            lecturer.setSpecialization(form.getSpecialization() != null ? form.getSpecialization().trim() : null);
            lecturerRepository.save(lecturer);
        }
    }
}
