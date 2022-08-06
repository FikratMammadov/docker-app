package com.fikrat.bookstore.service;

import com.fikrat.bookstore.dto.book.AddBookDto;
import com.fikrat.bookstore.dto.book.BookDetailDto;
import com.fikrat.bookstore.dto.book.UpdateBookDto;
import com.fikrat.bookstore.model.Book;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


public interface BookService {
    Book add(Principal principal, AddBookDto addBookDto, MultipartFile file) throws Exception;
    Book update(Principal principal, UpdateBookDto updateBookDto, MultipartFile file) throws Exception;
    List<BookDetailDto> getAll();
    List<BookDetailDto> getAll(int pageNo,int pageSize);
    List<BookDetailDto> getAllByName(String name, int pageNo,int pageSize);
    BookDetailDto getById(int id);
    List<BookDetailDto> getAllByPublisherId(int id);
}
