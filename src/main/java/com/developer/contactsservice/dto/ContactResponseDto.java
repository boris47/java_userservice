package com.developer.contactsservice.dto;

public record ContactResponseDto(
		String uniqueId,
		String name,
		String surname,
		String number,
		String lastUpdate,
		String imageURL,
		String[] tags
) {}
