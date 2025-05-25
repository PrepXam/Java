package com.java.ne_starter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "owners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Owner extends Base {

    @Column(name = "name")
    private String name;

    @Column(name = "national_id", unique = true)
    private String nationalId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<PlateNumber> plateNumbers;
}