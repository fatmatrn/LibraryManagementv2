package com.turan.librarymanagement.controller;

import com.turan.librarymanagement.dto.LoanResponse;
import com.turan.librarymanagement.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/borrow/{title}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<LoanResponse> createLoan(@PathVariable String title){
                LoanResponse response=loanService.createLoan(title);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/return/{title}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<LoanResponse> returnBook(@ PathVariable String title){
        LoanResponse response=loanService.returnBook(title);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponse>> getAllLoans(){
        List<LoanResponse> loanResponseList= loanService.getAllLoans();
        return new ResponseEntity<>(loanResponseList,HttpStatus.OK);
    }
}
