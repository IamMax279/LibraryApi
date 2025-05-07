package com.max420.books.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
public class BookDto {
    private Long id;
    private String author;
    private String title;
    private String category;
    private Integer pages;
    private Integer quantity;

    @Override
    public String toString() {
        return "BookDto{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", pages=" + pages +
                ", quantity=" + quantity +
                '}';
    }

    public Map<String, String> format() {
        Map<String, String> values = new HashMap<>();
        values.put("author", author);
        values.put("title", title);
        values.put("category", category);
        values.put("pages", pages.toString());
        values.put("quantity", quantity.toString());

        return values;
    }
}