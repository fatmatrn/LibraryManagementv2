package com.turan.librarymanagement.controller;

import com.turan.librarymanagement.dto.BookDTO;
import com.turan.librarymanagement.repository.BookRepository;
import com.turan.librarymanagement.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
            BookDTO bookDTO1 =bookService.createBook(bookDTO);
        return new ResponseEntity<>(bookDTO1, HttpStatus.CREATED);
    }

    @GetMapping("/{title}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public  ResponseEntity<BookDTO> getBook(@PathVariable String title){
        BookDTO bookDTO =bookService.getBook(title);
       return ResponseEntity.ok(bookDTO);
    }

    @DeleteMapping("/borrow/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeBook(@PathVariable String title){
        String message=bookService.removeBook(title);
        return ResponseEntity.ok(message);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> bookDTOList =bookService.getAllBooks();
        return ResponseEntity.ok(bookDTOList);
    }





}
