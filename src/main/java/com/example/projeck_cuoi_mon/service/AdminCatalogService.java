package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.request.DepartmentForm;
import com.example.projeck_cuoi_mon.dto.request.LabRoomTypeForm;
import com.example.projeck_cuoi_mon.dto.request.MajorForm;
import com.example.projeck_cuoi_mon.dto.response.AcademicCatalogStats;
import com.example.projeck_cuoi_mon.dto.response.DepartmentView;
import com.example.projeck_cuoi_mon.dto.response.LabRoomTypeView;
import com.example.projeck_cuoi_mon.dto.response.MajorView;
import com.example.projeck_cuoi_mon.model.Department;
import com.example.projeck_cuoi_mon.model.LabRoomType;
import com.example.projeck_cuoi_mon.model.Lecturer;
import com.example.projeck_cuoi_mon.model.Major;
import com.example.projeck_cuoi_mon.model.enums.EquipmentStatus;
import com.example.projeck_cuoi_mon.repository.DepartmentRepository;
import com.example.projeck_cuoi_mon.repository.EquipmentRepository;
import com.example.projeck_cuoi_mon.repository.LabRoomTypeRepository;
import com.example.projeck_cuoi_mon.repository.LecturerRepository;
import com.example.projeck_cuoi_mon.repository.MajorRepository;
import com.example.projeck_cuoi_mon.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCatalogService {

    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;
    private final LecturerRepository lecturerRepository;
    private final UserProfileRepository userProfileRepository;
    private final LabRoomTypeRepository labRoomTypeRepository;
    private final EquipmentRepository equipmentRepository;

    @Transactional(readOnly = true)
    public List<DepartmentView> findDepartmentViews() {
        return departmentRepository.findAllOrderByName().stream()
                .map(this::toDepartmentView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Department> findAllDepartments() {
        return departmentRepository.findAllOrderByName();
    }

    @Transactional(readOnly = true)
    public List<MajorView> findAllMajorViews() {
        return majorRepository.findAllWithDepartment().stream()
                .map(this::toMajorView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LabRoomTypeView> findLabRoomTypeViews() {
        return labRoomTypeRepository.findAll().stream()
                .sorted(Comparator.comparing(LabRoomType::getName))
                .map(type -> new LabRoomTypeView(
                        type.getId(),
                        type.getCode(),
                        type.getName(),
                        type.getDescription(),
                        type.getActive(),
                        equipmentRepository.countByLabRoomTypeId(type.getId()),
                        equipmentRepository.countByLabRoomTypeIdAndActiveTrue(type.getId()),
                        equipmentRepository.countByLabRoomTypeIdAndStatus(type.getId(), EquipmentStatus.MAINTENANCE),
                        equipmentRepository.sumQuantityTotalByLabRoomTypeId(type.getId()),
                        equipmentRepository.sumQuantityAvailableByLabRoomTypeId(type.getId())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public LabRoomTypeForm getLabRoomTypeForm(Long id) {
        LabRoomType labRoomType = findLabRoomType(id);
        LabRoomTypeForm form = new LabRoomTypeForm();
        form.setCode(labRoomType.getCode());
        form.setName(labRoomType.getName());
        form.setDescription(labRoomType.getDescription());
        form.setActive(labRoomType.getActive());
        return form;
    }

    @Transactional(readOnly = true)
    public AcademicCatalogStats getStats() {
        return new AcademicCatalogStats(
                departmentRepository.count(),
                departmentRepository.countByActiveTrue(),
                majorRepository.count(),
                majorRepository.countByActiveTrue(),
                lecturerRepository.count(),
                userProfileRepository.count()
        );
    }

    @Transactional(readOnly = true)
    public DepartmentForm getDepartmentForm(Long id) {
        Department department = findDepartment(id);
        DepartmentForm form = new DepartmentForm();
        form.setCode(department.getCode());
        form.setName(department.getName());
        form.setDescription(department.getDescription());
        form.setActive(department.getActive());
        return form;
    }

    @Transactional(readOnly = true)
    public MajorForm getMajorForm(Long id) {
        Major major = findMajor(id);
        MajorForm form = new MajorForm();
        form.setDepartmentId(major.getDepartment().getId());
        form.setCode(major.getCode());
        form.setName(major.getName());
        form.setDescription(major.getDescription());
        form.setActive(major.getActive());
        return form;
    }

    @Transactional
    public void createDepartment(DepartmentForm form) {
        String code = normalizeCode(form.getCode());
        departmentRepository.findByCode(code).ifPresent(existing -> {
            throw new IllegalArgumentException("Ma khoa da ton tai");
        });

        Department department = Department.builder()
                .code(code)
                .name(form.getName().trim())
                .description(trimToNull(form.getDescription()))
                .active(Boolean.TRUE.equals(form.getActive()))
                .build();
        departmentRepository.save(department);
    }

    @Transactional
    public void updateDepartment(Long id, DepartmentForm form) {
        Department department = findDepartment(id);
        String code = normalizeCode(form.getCode());
        departmentRepository.findByCode(code)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ma khoa da ton tai");
                });

        department.setCode(code);
        department.setName(form.getName().trim());
        department.setDescription(trimToNull(form.getDescription()));
        department.setActive(Boolean.TRUE.equals(form.getActive()));
    }

    @Transactional
    public void createMajor(MajorForm form) {
        String code = normalizeCode(form.getCode());
        majorRepository.findByCode(code).ifPresent(existing -> {
            throw new IllegalArgumentException("Ma nganh da ton tai");
        });

        Department department = findDepartment(form.getDepartmentId());
        Major major = Major.builder()
                .department(department)
                .code(code)
                .name(form.getName().trim())
                .description(trimToNull(form.getDescription()))
                .active(Boolean.TRUE.equals(form.getActive()))
                .build();
        majorRepository.save(major);
    }

    @Transactional
    public void updateMajor(Long id, MajorForm form) {
        Major major = findMajor(id);
        String code = normalizeCode(form.getCode());
        majorRepository.findByCode(code)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ma nganh da ton tai");
                });

        major.setDepartment(findDepartment(form.getDepartmentId()));
        major.setCode(code);
        major.setName(form.getName().trim());
        major.setDescription(trimToNull(form.getDescription()));
        major.setActive(Boolean.TRUE.equals(form.getActive()));
    }

    @Transactional
    public void disableDepartment(Long id) {
        findDepartment(id).setActive(false);
    }

    @Transactional
    public void disableMajor(Long id) {
        findMajor(id).setActive(false);
    }

    @Transactional
    public void createLabRoomType(LabRoomTypeForm form) {
        String code = normalizeCode(form.getCode());
        labRoomTypeRepository.findByCode(code).ifPresent(existing -> {
            throw new IllegalArgumentException("Ma danh muc phong lab da ton tai");
        });

        LabRoomType labRoomType = LabRoomType.builder()
                .code(code)
                .name(form.getName().trim())
                .description(trimToNull(form.getDescription()))
                .active(Boolean.TRUE.equals(form.getActive()))
                .build();
        labRoomTypeRepository.save(labRoomType);
    }

    @Transactional
    public void updateLabRoomType(Long id, LabRoomTypeForm form) {
        LabRoomType labRoomType = findLabRoomType(id);
        String code = normalizeCode(form.getCode());
        labRoomTypeRepository.findByCode(code)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ma danh muc phong lab da ton tai");
                });

        labRoomType.setCode(code);
        labRoomType.setName(form.getName().trim());
        labRoomType.setDescription(trimToNull(form.getDescription()));
        labRoomType.setActive(Boolean.TRUE.equals(form.getActive()));
    }

    @Transactional
    public void disableLabRoomType(Long id) {
        findLabRoomType(id).setActive(false);
    }

    private DepartmentView toDepartmentView(Department department) {
        List<MajorView> majors = majorRepository.findByDepartmentIdOrderByNameAsc(department.getId()).stream()
                .map(this::toMajorView)
                .toList();
        List<Lecturer> lecturers = lecturerRepository.findByDepartmentIdWithUser(department.getId());
        String headLecturerName = lecturers.stream()
                .findFirst()
                .map(lecturer -> {
                    String title = lecturer.getAcademicTitle();
                    String name = lecturer.getUser().getFullName();
                    return title == null || title.isBlank() ? name : title + " " + name;
                })
                .orElse("Chua gan");

        return new DepartmentView(
                department.getId(),
                department.getCode(),
                department.getName(),
                department.getDescription(),
                department.getActive(),
                headLecturerName,
                lecturerRepository.countByDepartmentId(department.getId()),
                userProfileRepository.countByDepartmentId(department.getId()),
                majors.size(),
                majors
        );
    }

    private MajorView toMajorView(Major major) {
        Department department = major.getDepartment();
        return new MajorView(
                major.getId(),
                department.getId(),
                department.getName(),
                major.getCode(),
                major.getName(),
                major.getDescription(),
                major.getActive()
        );
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay khoa"));
    }

    private Major findMajor(Long id) {
        return majorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay nganh"));
    }

    private LabRoomType findLabRoomType(Long id) {
        return labRoomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay danh muc phong lab"));
    }

    private String normalizeCode(String code) {
        return code == null ? "" : code.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
