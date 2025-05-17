package com.java.ne_starter.dtos.user;

import com.java.ne_starter.dtos.auth.RegisterDto;
import com.java.ne_starter.enumerations.user.EUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateUserDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    private String phoneNumber;
    private String nationalId;
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;


    public static CreateUserDto fromRegisterDto(RegisterDto dto) {
        CreateUserDto userDto = new CreateUserDto();
        userDto.setFirstName(dto.getFirstName());
        userDto.setLastName(dto.getLastName());
        userDto.setEmail(dto.getEmail());
        userDto.setPassword(dto.getPassword());
        userDto.setNationalId(dto.getNationalId());
        userDto.setPhoneNumber(dto.getPhoneNumber());
        return userDto;
    }
}



