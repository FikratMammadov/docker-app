package com.fikrat.bookstore.mapper;

import com.fikrat.bookstore.dto.book.AddBookDto;
import com.fikrat.bookstore.dto.book.BookDetailDto;
import com.fikrat.bookstore.dto.book.UpdateBookDto;
import com.fikrat.bookstore.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    Book addBookDtoToBook(AddBookDto addBookDto);
    List<BookDetailDto> booksToBookDetailsDto(List<Book> books);
    BookDetailDto bookToBookDetailDto(Book book);
    Book updateBookDtoToBook(UpdateBookDto updateBookDto);
}
