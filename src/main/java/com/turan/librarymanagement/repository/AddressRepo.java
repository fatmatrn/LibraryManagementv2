package com.turan.librarymanagement.repository;

import com.turan.librarymanagement.domain.Address;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AddressRepo  extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {

    List<Address> findAll(Specification<Address> spec);
}
