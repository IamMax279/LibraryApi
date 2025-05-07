package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BookServiceDto {
    private Long id;
    private String author;
    private String title;
    private String category;
    private Integer pages;
    private Integer quantity;
}
