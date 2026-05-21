package com.example.projeck_cuoi_mon.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IssueDetailView {

    private String equipmentName;
    private String equipmentCode;
    private Integer requestedQuantity;
    private Integer availableQuantity;
    private boolean enoughStock;
}
