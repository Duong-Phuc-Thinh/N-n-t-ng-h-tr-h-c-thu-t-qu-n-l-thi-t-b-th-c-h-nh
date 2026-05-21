package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.request.EquipmentForm;
import com.example.projeck_cuoi_mon.dto.response.AdminDashboardStats;
import com.example.projeck_cuoi_mon.model.Equipment;
import com.example.projeck_cuoi_mon.model.LabRoomType;
import com.example.projeck_cuoi_mon.repository.EquipmentRepository;
import com.example.projeck_cuoi_mon.repository.LabRoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private static final int LOW_STOCK_THRESHOLD = 2;

    private final EquipmentRepository equipmentRepository;
    private final LabRoomTypeRepository labRoomTypeRepository;

    @Transactional(readOnly = true)
    public List<Equipment> findAll() {
        return equipmentRepository.findAllWithLabRoomType();
    }

    @Transactional(readOnly = true)
    public List<Equipment> findAll(Long labRoomTypeId) {
        if (labRoomTypeId == null) {
            return findAll();
        }
        return equipmentRepository.findByLabRoomTypeIdWithLabRoomType(labRoomTypeId);
    }

    @Transactional(readOnly = true)
    public Equipment findById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thiết bị"));
    }

    @Transactional(readOnly = true)
    public EquipmentForm getFormForEdit(Long id) {
        Equipment equipment = findById(id);
        EquipmentForm form = new EquipmentForm();
        form.setLabRoomTypeId(equipment.getLabRoomType().getId());
        form.setName(equipment.getName());
        form.setDescription(equipment.getDescription());
        form.setTotalQuantity(equipment.getQuantityTotal());
        form.setAvailableQuantity(equipment.getQuantityAvailable());
        form.setStatus(equipment.getStatus());
        return form;
    }

    @Transactional
    public void create(EquipmentForm form) {
        validateQuantity(form);

        LabRoomType labRoomType = labRoomTypeRepository.findById(form.getLabRoomTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục phòng lab"));

        Equipment equipment = Equipment.builder()
                .labRoomType(labRoomType)
                .code(generateEquipmentCode())
                .name(form.getName().trim())
                .description(form.getDescription())
                .quantityTotal(form.getTotalQuantity())
                .quantityAvailable(form.getAvailableQuantity())
                .status(form.getStatus())
                .active(true)
                .build();

        equipmentRepository.save(equipment);
    }

    @Transactional
    public void update(Long id, EquipmentForm form) {
        validateQuantity(form);

        LabRoomType labRoomType = labRoomTypeRepository.findById(form.getLabRoomTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục phòng lab"));

        Equipment equipment = findById(id);
        equipment.setLabRoomType(labRoomType);
        equipment.setName(form.getName().trim());
        equipment.setDescription(form.getDescription());
        equipment.setQuantityTotal(form.getTotalQuantity());
        equipment.setQuantityAvailable(form.getAvailableQuantity());
        equipment.setStatus(form.getStatus());
    }

    @Transactional
    public void disable(Long id) {
        Equipment equipment = findById(id);
        equipment.setActive(false);
    }

    @Transactional(readOnly = true)
    public List<LabRoomType> findAllLabRoomTypes() {
        return labRoomTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AdminDashboardStats getDashboardStats() {
        return new AdminDashboardStats(
                equipmentRepository.count(),
                equipmentRepository.countLowStock(LOW_STOCK_THRESHOLD),
                equipmentRepository.countByActiveTrue(),
                equipmentRepository.countByActiveFalse()
        );
    }

    private void validateQuantity(EquipmentForm form) {
        if (form.getAvailableQuantity() != null
                && form.getTotalQuantity() != null
                && form.getAvailableQuantity() > form.getTotalQuantity()) {
            throw new IllegalArgumentException("Số lượng khả dụng không được lớn hơn tổng số lượng");
        }
    }

    private LabRoomType findDefaultLabRoomType() {
        return labRoomTypeRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vui lòng khởi tạo danh mục phòng lab trước khi tạo thiết bị"));
    }

    private String generateEquipmentCode() {
        return "EQ-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
