package com.turan.librarymanagement.service;

import com.turan.librarymanagement.domain.Book;
import com.turan.librarymanagement.domain.Borrower;
import com.turan.librarymanagement.domain.Loan;
import com.turan.librarymanagement.dto.LoanResponse;
import com.turan.librarymanagement.exception.ErrorMessage;
import com.turan.librarymanagement.exception.ResourceNotFoundException;
import com.turan.librarymanagement.repository.LoanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    private final BorrowerService borrowerService;
    private final BookService bookService;
    private final LoanRepository loanRepository;
    public LoanService(BorrowerService borrowerService, BookService bookService, LoanRepository loanRepository) {
        this.borrowerService = borrowerService;
        this.bookService = bookService;
        this.loanRepository = loanRepository;
    }
    public LoanResponse createLoan(String title) {
        Borrower borrower=borrowerService.getCurrentBorrower();
       Book book= bookService.callBook(title);
       // Book Control
        if (!(book.getAvailableCopies()>0)){
            throw new ResourceNotFoundException(ErrorMessage.NOT_AVAILABLE_BOOK_MESSAGE);
        }

        List<String> listBookTitle= loansList(borrower.getEmail());
        if (listBookTitle.size()>=3 ){
            throw new ResourceNotFoundException(ErrorMessage.NOT_RETURNED_BOOK_MESSAGE);
        }
        else if (listBookTitle.contains(title)) {
            throw new ResourceNotFoundException(ErrorMessage.SAME_BOOK_MESSAGE);
        }
        book.setAvailableCopies(book.getAvailableCopies()-1);

        Loan loan =new Loan();
        loan.setBook(book);
        loan.setBorrower(borrower);
        loan.setBorrowDate(LocalDate.now());
        loan.setStatus(false);
        loanRepository.save(loan);
        return loanToLoanResponse(loan);
    }

    public LoanResponse returnBook(String title) {
        Borrower borrower=borrowerService.getCurrentBorrower();
        Book book= bookService.callBook(title);
        List<String> bookList=loansList(borrower.getEmail());

        if (!bookList.contains(title)){
            throw new ResourceNotFoundException(ErrorMessage.NOT_EXIST_YOUR_LIST);
        }
        // title ve mail ile get loan
        Loan loan =getLoan(borrower.getEmail(), title);
        // patch işlemi
        book.setAvailableCopies(book.getAvailableCopies()+1);
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(true);
        loanRepository.save(loan);
        return loanToLoanResponse(loan);
    }
    public List<LoanResponse> getAllLoans() {
        List<Loan> LoanList=loanRepository.findAll();
        List<LoanResponse> loanResponseList = new ArrayList<>();
        for(Loan loan : LoanList){
            loanResponseList.add(loanToLoanResponse(loan));
        }
        return loanResponseList;
    }

    //assist methods
    public List<String> loansList( String email){
       List<String> bookList= loanRepository.findAllBookByEmail(email);
       return bookList;
    }
    public Loan getLoan(String email, String title){
        Loan loan=loanRepository.findLoanWithEmailAndTitle(email,title);
        return loan;
    }
    public boolean getLoanByBook(String title){
       List<Loan> loan= loanRepository.findByBookTitle(title);
        if (loan.isEmpty()) {
            return true;
        }
        return false;
    }
    //Mapping
    public LoanResponse loanToLoanResponse(Loan loan){
        LoanResponse loanResponse =new LoanResponse();
        loanResponse.setStatus(loan.isStatus());
        loanResponse.setBorrowDate(loan.getBorrowDate());
        loanResponse.setBookTitle(loan.getBook().getTitle());
        loanResponse.setBorrowerEmail(loan.getBorrower().getEmail());
        loanResponse.setReturnDate(loan.getReturnDate());
        return loanResponse;
    }

}
