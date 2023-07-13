package com.turan.librarymanagement.repository;

import com.turan.librarymanagement.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    //Bu query maile ait status false olan book titlelerı list olarak getirir
    @Query("select l.book.title from Loan l join fetch Borrower b on l.borrower=b.id " +
            "where b.email=:email and l.status=false")
    List<String> findAllBookByEmail(@Param("email") String email);


    @Query("select l from Loan l join fetch Borrower b on l.borrower=b.id " +
            "join fetch Book book on l.book=book.id " +
            "where b.email=:email and l.status=false and book.title =:title")
    Loan findLoanWithEmailAndTitle(String email, String title);

    @Query("select l from Loan l join fetch Book book on l.book=book.id " +
            "where l.status=false and book.title =:title")
    List<Loan> findByBookTitle(@Param("title") String title);

}
