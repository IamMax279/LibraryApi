package com.max420.books.services;


import com.max420.books.dto.AddBookRequest;
import com.max420.books.dto.BookDto;

import java.util.List;
import java.util.Map;

public interface BookService {
    void addBook(AddBookRequest request) throws Exception;
    List<Map<String, String>> getAllBooks();
    Map<String, String> findByTitle(String title) throws Exception;
    void rentABook(String title) throws Exception;
    void bringBookBack(String title) throws Exception;
}
