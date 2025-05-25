package com.java.ne_starter.models;

import com.java.ne_starter.enumerations.user.EUserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Base {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private EUserRole name;
}
