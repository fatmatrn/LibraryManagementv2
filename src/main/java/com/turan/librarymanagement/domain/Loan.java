package com.turan.librarymanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_loans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean status;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    @OneToOne
    private Book book;
    @OneToOne
    private  Borrower borrower;
}
