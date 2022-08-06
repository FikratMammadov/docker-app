package com.fikrat.bookstore.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailDto {
    private String publisherName;
    private String name;
    private String author;
    private BigDecimal price;
    private String description;
    private byte[] image;
}
