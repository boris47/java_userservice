package com.example.userservice.model;

import com.example.userservice.dto.UserDto;
import com.example.userservice.enums.*;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "users")
public class User
{
    @Id
    private String id;
    
    private String name;
    private int age;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    public static User From(String InUniqueId, UserDto InDto)
    {
        return new User(InUniqueId, InDto.getName(), InDto.getAge(), InDto.getRole());
    }
}
