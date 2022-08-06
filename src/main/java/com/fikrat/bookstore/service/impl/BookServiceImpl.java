package com.fikrat.bookstore.service.impl;

import com.fikrat.bookstore.constants.Messages;
import com.fikrat.bookstore.dto.book.AddBookDto;
import com.fikrat.bookstore.dto.book.BookDetailDto;
import com.fikrat.bookstore.dto.book.UpdateBookDto;
import com.fikrat.bookstore.exception.BookNotFoundException;
import com.fikrat.bookstore.exception.PublisherNotFoundException;
import com.fikrat.bookstore.exception.UserNotFoundException;
import com.fikrat.bookstore.mapper.BookMapper;
import com.fikrat.bookstore.model.Book;
import com.fikrat.bookstore.model.Role;
import com.fikrat.bookstore.model.User;
import com.fikrat.bookstore.model.enums.ERole;
import com.fikrat.bookstore.repository.BookRepository;
import com.fikrat.bookstore.repository.RoleRepository;
import com.fikrat.bookstore.repository.UserRepository;
import com.fikrat.bookstore.service.BookService;
import com.fikrat.bookstore.util.uploader.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RoleRepository roleRepository;
    private final BookMapper mapper;

    @Override
    public Book add(Principal principal, AddBookDto addBookDto, MultipartFile file) throws Exception {
        User publisher = userRepository.findByUsername(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Username not found with name " + principal.getName()));
        Book book = mapper.addBookDtoToBook(addBookDto);
        book.setImage(FileUploader.upload(file));
        book.setPublisher(publisher);
        bookRepository.save(book);
        return book;
    }

    @Override
    public Book update(Principal principal, UpdateBookDto updateBookDto, MultipartFile file) throws Exception {
        User publisher = userRepository.findByUsername(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Username not found with name " + principal.getName()));

        Book book = bookRepository.findById(updateBookDto.getId())
                .orElseThrow(() -> new BookNotFoundException(Messages.BOOK_NOT_FOUND));
        if (!publisher.getBooks().contains(book)){
            throw new BookNotFoundException(Messages.BOOK_NOT_FOUND);
        }
        book = mapper.updateBookDtoToBook(updateBookDto);
        book.setImage(FileUploader.upload(file));
        book.setPublisher(publisher);
        bookRepository.save(book);
        return book;
    }

    @Override
    public List<BookDetailDto> getAll() {
        var books = bookRepository.findAll();
        return mapper.booksToBookDetailsDto(books);
    }

    @Override
    public List<BookDetailDto> getAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        var books = bookRepository.findAll(pageable).getContent();
        return mapper.booksToBookDetailsDto(books);
    }

    @Override
    public List<BookDetailDto> getAllByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        var books = bookRepository.getByNameContainingIgnoreCase(name,pageable).getContent();
        return mapper.booksToBookDetailsDto(books);
    }


    @Override
    public BookDetailDto getById(int id) {
        var book = bookRepository.findById(id)
                .orElseThrow(()->new BookNotFoundException(Messages.ROLE_NOT_FOUND));
        return mapper.bookToBookDetailDto(book);
    }

    @Override
    public List<BookDetailDto> getAllByPublisherId(int id) {
        var publisher = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(Messages.USER_NOT_FOUND));

        Role rolePublisher = roleRepository.findByName(ERole.ROLE_PUBLISHER)
                .orElse(Role.builder().name(ERole.ROLE_PUBLISHER).build());

        var roles = publisher.getRoles();
        if (!roles.contains(rolePublisher)){
            throw new PublisherNotFoundException(Messages.PUBLISHER_NOT_FOUND);
        }

        var books = bookRepository.findAllByPublisher(publisher);
        return mapper.booksToBookDetailsDto(books);
    }
}
