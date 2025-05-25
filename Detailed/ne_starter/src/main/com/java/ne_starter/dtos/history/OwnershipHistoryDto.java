package com.java.ne_starter.dtos.history;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OwnershipHistoryDto {

    private String ownerName;
    private String prevOwnerName;
    private String prevOwnerNationalId;
    private String ownerNationalId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal purchasePrice;
}
