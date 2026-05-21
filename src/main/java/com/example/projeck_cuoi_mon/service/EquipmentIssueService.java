package com.example.projeck_cuoi_mon.service;

import com.example.projeck_cuoi_mon.dto.response.IssueDetailView;
import com.example.projeck_cuoi_mon.dto.response.IssueRecordView;
import com.example.projeck_cuoi_mon.model.BorrowingDetail;
import com.example.projeck_cuoi_mon.model.BorrowingRecord;
import com.example.projeck_cuoi_mon.model.Equipment;
import com.example.projeck_cuoi_mon.model.User;
import com.example.projeck_cuoi_mon.model.enums.BorrowingStatus;
import com.example.projeck_cuoi_mon.repository.BorrowingRecordRepository;
import com.example.projeck_cuoi_mon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentIssueService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<IssueRecordView> findWaitingAllocationRecords() {
        return borrowingRecordRepository.findIssueQueue(BorrowingStatus.WAITING_ALLOCATION)
                .stream()
                .map(this::toView)
                .toList();
    }

    @Transactional
    public void confirmIssue(Long borrowingRecordId, Long adminUserId) {
        BorrowingRecord record = borrowingRecordRepository.findByIdForIssue(borrowingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bản ghi mượn thiết bị"));

        if (record.getStatus() != BorrowingStatus.WAITING_ALLOCATION) {
            throw new IllegalArgumentException("Chỉ những bản ghi đang chờ cấp phát mới có thể được duyệt cấp phát");
        }

        if (record.getDetails().isEmpty()) {
            throw new IllegalArgumentException("Bản ghi mượn thiết bị không có thông tin chi tiết thiết bị");
        }

        validateAllStockAvailable(record);
        subtractStock(record);

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản quản trị viên"));
        record.setExportedByAdmin(admin);
        record.setStatus(BorrowingStatus.ISSUED);
    }

    private void validateAllStockAvailable(BorrowingRecord record) {
        for (BorrowingDetail detail : record.getDetails()) {
            Equipment equipment = detail.getEquipment();
            if (!Boolean.TRUE.equals(equipment.getActive())) {
                throw new IllegalArgumentException(equipment.getName() + " đã ngừng hoạt động");
            }
            if (equipment.getQuantityAvailable() < detail.getQuantity()) {
                throw new IllegalArgumentException(
                        equipment.getName() + " không đủ số lượng khả dụng. Yêu cầu "
                                + detail.getQuantity() + ", hiện có "
                                + equipment.getQuantityAvailable()
                );
            }
        }
    }

    private void subtractStock(BorrowingRecord record) {
        for (BorrowingDetail detail : record.getDetails()) {
            Equipment equipment = detail.getEquipment();
            equipment.setQuantityAvailable(equipment.getQuantityAvailable() - detail.getQuantity());
        }
    }

    private IssueRecordView toView(BorrowingRecord record) {
        List<IssueDetailView> details = record.getDetails()
                .stream()
                .map(this::toDetailView)
                .toList();

        return IssueRecordView.builder()
                .id(record.getId())
                .studentName(record.getStudent().getFullName())
                .purpose(record.getPurpose())
                .borrowDate(record.getBorrowDate())
                .expectedReturnDate(record.getExpectedReturnDate())
                .details(details)
                .issuable(details.stream().allMatch(IssueDetailView::isEnoughStock))
                .build();
    }

    private IssueDetailView toDetailView(BorrowingDetail detail) {
        Equipment equipment = detail.getEquipment();
        return IssueDetailView.builder()
                .equipmentName(equipment.getName())
                .equipmentCode(equipment.getCode())
                .requestedQuantity(detail.getQuantity())
                .availableQuantity(equipment.getQuantityAvailable())
                .enoughStock(Boolean.TRUE.equals(equipment.getActive())
                        && equipment.getQuantityAvailable() >= detail.getQuantity())
                .build();
    }
}
