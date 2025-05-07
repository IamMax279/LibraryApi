package com.max420.books.utils;

import com.max420.books.dto.BookDto;

public class BookServiceUtils {
    public static boolean isBookDtoValid(BookDto bookDto) {
        return !bookDto.getAuthor().isEmpty() && !bookDto.getTitle().isEmpty() && !bookDto.getCategory().isEmpty() && bookDto.getPages() > 0 && bookDto.getQuantity() > 0;
    }
}
