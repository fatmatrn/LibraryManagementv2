package com.turan.librarymanagement.repository;

import com.turan.librarymanagement.domain.Person;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PersonRepo extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    List<Person> findAll(Specification<Person> spec);

    Optional<Person> findById(Specification<Person> spec);
}