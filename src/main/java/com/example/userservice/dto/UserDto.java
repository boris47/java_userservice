package com.example.userservice.dto;

import com.example.userservice.enums.*;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserDto
{
    @NotBlank(message = "Nome obbligatorio")
    private String name;
    @Min(value = 1, message = "Età deve essere almeno 1")
    private int age;
    @NotNull(message = "Campo 'role' obbligatorio")
    private UserRole role;
}
