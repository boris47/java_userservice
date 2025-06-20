package com.developer.contactsservice.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record ContactUpsertDto(
		String uniqueId,
		String name,
		String surname,
		@Pattern(
			regexp = "^[+]?\\d{1,3}?[\\s.-]?\\(?\\d{1,4}\\)?[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,9}$",
			message = "Invalid phone number format"
			// Accepts: +39 333 1234567, 3331234567, (02) 1234567, +1-800-123-4567
		)
		String number,
		String secondName,
		@Pattern(regexp = "^(https|ftp)://[^\\s/$.?#].[^\\s]*$", message = "Invalid URL")
		String ImageURL,
		@Email(message = "Invalid Email")
		String email,
		List<
			@NotBlank
			@Pattern(regexp = "^[A-Za-z]+$", message = "Tags must be a single word with letters only")
		String> tags
) {}
