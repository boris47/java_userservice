package com.developer.contactsservice.dto;

import lombok.*;

import java.util.List;

import jakarta.validation.constraints.*;

@Data
@RequiredArgsConstructor
public class ContactsDto
{
	@NotBlank(message = "Name is mandatory")
	private final String name;
	
	@NotBlank(message = "Surname is mandatory")
	private final String surname;
	
	@NotBlank(message = "Telephone number is mandatory")
	private final String number;
	
	private final String secondName;
	
	@Email(message = "Invalid email format")
	private final String email;
	
	@NotNull
	private final List<@NotBlank String> tags;
}
