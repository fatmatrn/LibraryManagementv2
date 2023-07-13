package com.turan.librarymanagement.repository;

import com.turan.librarymanagement.domain.Borrower;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {


   @EntityGraph(attributePaths = "roles")
    Optional<Borrower> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
