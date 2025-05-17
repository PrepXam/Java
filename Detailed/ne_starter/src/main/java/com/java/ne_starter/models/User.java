package com.java.ne_starter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.ne_starter.models.Base;
import com.java.ne_starter.models.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@NoArgsConstructor
@AllArgsConstructor
public class User extends Base {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name="national_id")
    private String nationalId;

    @JsonIgnore
    @Size(min=8)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Transient
    private String fullName;

    @Transient
    public String getFullName() {
        return String.format("%s %s", firstName, lastName).trim();
    }

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(nullable = true)
    private String otp;

    @Column(nullable = true)
    private LocalDateTime otpExpirationTime;

    public User(String email, String firstName, String lastName, String password,String nationalId,String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nationalId = nationalId;
    }
}
