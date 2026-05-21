package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.request.LoginRequest;
import com.example.projeck_cuoi_mon.dto.request.RegisterRequest;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.model.Department;
import com.example.projeck_cuoi_mon.model.Lecturer;
import com.example.projeck_cuoi_mon.model.User;
import com.example.projeck_cuoi_mon.model.UserProfile;
import com.example.projeck_cuoi_mon.model.enums.UserRole;
import com.example.projeck_cuoi_mon.model.enums.UserStatus;
import com.example.projeck_cuoi_mon.repository.DepartmentRepository;
import com.example.projeck_cuoi_mon.repository.LecturerRepository;
import com.example.projeck_cuoi_mon.repository.UserProfileRepository;
import com.example.projeck_cuoi_mon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final LecturerRepository lecturerRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SessionUser register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User user = User.builder()
                .email(request.getEmail().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .phone(request.getPhone())
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();
        User savedUser = userRepository.save(user);

        if (request.getRole() == UserRole.STUDENT) {
            createStudentProfile(savedUser, request);
        }

        if (request.getRole() == UserRole.LECTURER) {
            createLecturerProfile(savedUser, request);
        }

        return toSessionUser(savedUser);
    }

    @Transactional(readOnly = true)
    public SessionUser login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Email hoặc mật khẩu không chính xác"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Tài khoản hiện đang bị khóa hoặc ngừng hoạt động");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không chính xác");
        }

        return toSessionUser(user);
    }

    private void createStudentProfile(User user, RegisterRequest request) {
        Department department = findRequiredDepartment(request.getDepartmentId());
        String studentCode = requiredText(request.getStudentCode(), "Mã số sinh viên không được để trống");
        String className = requiredText(request.getClassName(), "Tên lớp không được để trống");

        if (userProfileRepository.findByStudentCode(studentCode).isPresent()) {
            throw new IllegalArgumentException("Mã số sinh viên đã tồn tại");
        }

        UserProfile profile = UserProfile.builder()
                .user(user)
                .department(department)
                .studentCode(studentCode)
                .className(className)
                .build();
        userProfileRepository.save(profile);
    }

    private void createLecturerProfile(User user, RegisterRequest request) {
        Department department = findRequiredDepartment(request.getDepartmentId());
        String lecturerCode = requiredText(request.getLecturerCode(), "Mã giảng viên không được để trống");

        if (lecturerRepository.findByLecturerCode(lecturerCode).isPresent()) {
            throw new IllegalArgumentException("Mã giảng viên đã tồn tại");
        }

        Lecturer lecturer = Lecturer.builder()
                .user(user)
                .department(department)
                .lecturerCode(lecturerCode)
                .academicTitle(requiredText(request.getAcademicTitle(), "Học hàm/học vị không được để trống"))
                .specialization(requiredText(request.getSpecialty(), "Chuyên môn không được để trống"))
                .build();
        lecturerRepository.save(lecturer);
    }

    private Department findRequiredDepartment(Long departmentId) {
        if (departmentId == null) {
            throw new IllegalArgumentException("Khoa quản lý không được để trống");
        }
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khoa"));
    }

    private String requiredText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private String emptyToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private SessionUser toSessionUser(User user) {
        return new SessionUser(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }
}
