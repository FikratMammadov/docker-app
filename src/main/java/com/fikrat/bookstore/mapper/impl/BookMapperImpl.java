package com.fikrat.bookstore.mapper.impl;

import com.fikrat.bookstore.dto.book.AddBookDto;
import com.fikrat.bookstore.dto.book.BookDetailDto;
import com.fikrat.bookstore.dto.book.UpdateBookDto;
import com.fikrat.bookstore.mapper.BookMapper;
import com.fikrat.bookstore.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Primary
@Component("customBookMapperImpl")
@RequiredArgsConstructor
public class BookMapperImpl implements BookMapper {
    @Override
    public Book addBookDtoToBook(AddBookDto addBookDto) {
        if (addBookDto == null) {
            return null;
        }

        Book book = new Book();
        book.setName(addBookDto.getName());
        book.setAuthor(addBookDto.getAuthor());
        book.setPrice(addBookDto.getPrice());
        book.setDescription(addBookDto.getDescription());
        return book;
    }

    @Override
    public List<BookDetailDto> booksToBookDetailsDto(List<Book> books) {
        if (books==null){
            return null;
        }

        var list = new ArrayList<BookDetailDto>(books.size());

        for (Book book : books){
            list.add(bookToBookDetailDto(book));
        }
        return list;
    }

    @Override
    public BookDetailDto bookToBookDetailDto(Book book) {
        if (book==null){
            return null;
        }

        var publisherName = book.getPublisher().getUsername();

        BookDetailDto bookDetailDto = new BookDetailDto();
        bookDetailDto.setPublisherName(publisherName);
        bookDetailDto.setName(book.getName());
        bookDetailDto.setAuthor(book.getAuthor());
        bookDetailDto.setPrice(book.getPrice());
        bookDetailDto.setImage(book.getImage());
        bookDetailDto.setDescription(book.getDescription());
        return bookDetailDto;
    }

    @Override
    public Book updateBookDtoToBook(UpdateBookDto updateBookDto) {
        if (updateBookDto == null) {
            return null;
        }

        Book book = new Book();
        book.setId(updateBookDto.getId());
        book.setName(updateBookDto.getName());
        book.setAuthor(updateBookDto.getAuthor());
        book.setPrice(updateBookDto.getPrice());
        book.setDescription(updateBookDto.getDescription());
        return book;
    }
}
