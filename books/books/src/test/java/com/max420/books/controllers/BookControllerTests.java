package com.max420.books.controllers;

import com.max420.books.BooksApplication;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BooksApplication.class)
@AutoConfigureMockMvc
@Transactional
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private String mockJsonContent = """
    {
        "bookDto": {
            "author": "Maks Kopciuch",
            "title": "testowa ksiazka",
            "category": "test",
            "pages": 69,
            "quantity": 1
        },
        "role": "librarian"
    }
    """;

    private String mockJsonImproperContent = """
    {
        "bookDto": {
            "author": "Maks Kopciuch",
            "title": "testowa ksiazka",
            "category": "test",
            "pages": 69,
            "quantity": 1
        },
        "role": "borrower"
    }
    """;

    private String mockJsonEmptyFields = """
    {
        "bookDto": {
            "author": "Maks Kopciuch",
            "title": "testowa ksiazka",
            "category": "",
            "pages": 69,
            "quantity": 0
        },
        "role": "librarian"
    }
    """;

    @Test
    void addBook_correctInput_returnsSuccess() throws Exception {
        mockMvc.perform(post("/books/addbook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockJsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("added a new book successfully"));
    }

    @Test
    void addBook_borrowerRole_returnsError() throws Exception {
        mockMvc.perform(post("/books/addbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockJsonImproperContent))
                .andExpect(status().isForbidden())
                .andExpect(content().string("no permission to insert new books"));
    }

    @Test
    void addBook_incompleteData_returnsError() throws Exception {
        mockMvc.perform(post("/books/addbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockJsonEmptyFields))
                .andExpect(status().isForbidden())
                .andExpect(content().string("invalid book data"));
    }

    @Test
    void getByTitle_titleInDb_returnsSuccess() throws Exception {
        mockMvc.perform(get("/books/getbytitle")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("title", "jack"))
                .andExpect(status().isOk());
    }

    @Test
    void getByTitle_titleNotInDb_returnsError() throws Exception {
        mockMvc.perform(get("/books/getbytitle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("title", "jacke"))
                .andExpect(status().isForbidden());
    }

    @Test
    void rentABook_titleInDb_returnsSuccess() throws Exception {
        mockMvc.perform(get("/books/rentabook")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("title", "jack"))
                .andExpect(status().isOk())
                .andExpect(content().string("successfully rented a book"));
    }

    @Test
    void rentABook_titleNotInDb_returnsError() throws Exception {
        mockMvc.perform(get("/books/rentabook")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("title","jacka"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("book with the given title was not found"));
    }

    @Test
    void rentABook_bookUnavailable_returnsError() throws Exception {
        mockMvc.perform(get("/books/rentabook")
                        .contentType(MediaType.APPLICATION_JSON)
                .queryParam("title","Road to success"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("this book is not available now"));
    }

    @Test
    void bringBookBack_bookHadBeenRented_returnSuccess() throws Exception {
        mockMvc.perform(get("/books/bringbookback")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("title", "Road to success"))
                .andExpect(status().isOk())
                .andExpect(content().string("book has been brought back successfully"));
    }

    @Test
    void bringBookBack_bookNotInLibrary_returnSuccess() throws Exception {
        mockMvc.perform(get("/books/bringbookback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("title", "Roat to success"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("book with the given title was not found"));
    }

    @Test
    void bringBookBack_bookNotRentedByAnyone_returnsError() throws Exception {
        mockMvc.perform(get("/books/bringbookback")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("title", "Roat to success"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("book with the given title was not found"));
    }
}