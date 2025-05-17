package com.java.ne_starter.models;

import com.java.ne_starter.enumerations.plate.PlateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "plate_numbers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlateNumber extends Base {

    @Column(name = "plate_number", unique = true, nullable = false)
    private String plateNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PlateStatus status = PlateStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private Owner owner;

    @Column(name = "issued_date", nullable = true)
    private LocalDate issuedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
