package com.developer.contactsservice.model;

import java.util.List;

import org.springframework.data.domain.Page;

public record CustomPaginatedListModel<T>
(
    List<T> data,
	MetaPaginationData pagination
) {
    public static <T> CustomPaginatedListModel<T> fromPage(Page<T> page)
	{
        return new CustomPaginatedListModel<>(
            page.getContent(),
            MetaPaginationData.fromPage(page)
        );
    }
}
/* RESULT
 {
   "data":[
      ...
   ],
   "pagination":{
      "page":XX,
      "size": XX,
      "totalElements":XX,
      "totalPages":XX,
      "last":true
   }
}
 */