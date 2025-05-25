package com.java.ne_starter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTransfer extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_owner_id")
    private Owner previousOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_owner_id")
    private Owner newOwner;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "transfer_date")
    private LocalDateTime transferDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_plate_id")
    private PlateNumber oldPlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_plate_id")
    private PlateNumber newPlate;
}