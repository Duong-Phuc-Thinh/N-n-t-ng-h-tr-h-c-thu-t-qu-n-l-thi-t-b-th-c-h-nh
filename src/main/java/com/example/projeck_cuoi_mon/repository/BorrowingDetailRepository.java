package com.example.projeck_cuoi_mon.repository;

import com.example.projeck_cuoi_mon.model.BorrowingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail, Long> {

    List<BorrowingDetail> findByBorrowingRecordId(Long borrowingRecordId);
}
