package com.max420.books.services.impl;

import com.max420.books.dto.AddBookRequest;
import com.max420.books.dto.BookDto;
import com.max420.books.models.Book;
import com.max420.books.repository.BookRepository;
import com.max420.books.services.BookService;
import com.max420.books.utils.BookServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public void addBook(AddBookRequest request) throws Exception {
        if(request.role().equals("librarian")) {
            if(BookServiceUtils.isBookDtoValid(request.bookDto()) && mapToBook(request.bookDto()) != null) {
                bookRepository.save(mapToBook(request.bookDto()));
            } else {
                throw new Exception("invalid book data");
            }
        } else {
            throw new Exception("no permission to insert new books");
        }
    }

    @Override
    public List<Map<String, String>> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(book -> mapToBookDto(book).format())
                .toList();
    }

    @Override
    public Map<String, String> findByTitle(String title) throws Exception {
        Map<String, String> res = mapToBookDto(bookRepository.findByTitle(title)).format();
        if (res == null) {
            throw new Exception("book with the given title was not found");
        }
        return res;
    }

    @Override
    public void rentABook(String title) throws Exception {
        Book book = bookRepository.findByTitle(title);
        if(book == null) {
            throw new Exception("book with the given title was not found");
        }
        if(book.getQuantity() <= 0) {
            throw new Exception("this book is not available now");
        }
        book.setQuantity(book.getQuantity()-1);
        bookRepository.save(book);
    }

    @Override
    public void bringBookBack(String title) throws Exception {
        Book book = bookRepository.findByTitle(title);
        if(book == null) {
            throw new Exception("book with the given title was not found");
        }
        book.setQuantity(book.getQuantity()+1);
        bookRepository.save(book);
    }

    private Book mapToBook(BookDto bookDto) {
        if(!BookServiceUtils.isBookDtoValid(bookDto)) {
            return null;
        }

        return Book.builder()
                .author(bookDto.getAuthor())
                .title(bookDto.getTitle())
                .category(bookDto.getCategory())
                .pages(bookDto.getPages())
                .quantity(bookDto.getQuantity())
                .build();
    }

    private BookDto mapToBookDto(Book book) {
        return BookDto.builder()
                .author(book.getAuthor())
                .title(book.getTitle())
                .category(book.getCategory())
                .pages(book.getPages())
                .quantity(book.getQuantity())
                .build();
    }
//    private <T, R> R map(T source, Class<R> targetClass) {
//        if (source instanceof Book && targetClass == BookDto.class) {
//            BookDto bookDto = (BookDto) source;
//            if(!BookServiceUtils.isBookDtoValid(bookDto)) {
//                return null;
//            }
//
//            return targetClass.cast(Book.builder()
//                    .author(bookDto.getAuthor())
//                    .title(bookDto.getTitle())
//                    .category(bookDto.getCategory())
//                    .pages(bookDto.getPages())
//                    .quantity(bookDto.getQuantity())
//                    .build());
//        }
//        else if (source instanceof BookDto && targetClass == Book.class) {
//            Book book = (Book) source;
//
//            return targetClass.cast(BookDto.builder()
//                    .author(book.getAuthor())
//                    .title(book.getTitle())
//                    .category(book.getCategory())
//                    .pages(book.getPages())
//                    .quantity(book.getQuantity())
//                    .build());
//        }
//        throw new IllegalArgumentException("You must either pass a Book or BookDto");
//    }
}
