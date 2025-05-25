package com.java.ne_starter.dtos.plate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AssignPlateNumberDto {
    @NotNull
    private UUID plateId;

    @NotNull
    private UUID ownerId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate issuedDate;
}
