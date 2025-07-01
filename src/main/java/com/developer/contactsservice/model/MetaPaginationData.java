package com.developer.contactsservice.model;

import org.springframework.data.domain.Page;

public record MetaPaginationData
(	
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean lastPage
)
{
	public static <T> MetaPaginationData fromPage(Page<T> page) {
        return new MetaPaginationData(
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
    }
}
