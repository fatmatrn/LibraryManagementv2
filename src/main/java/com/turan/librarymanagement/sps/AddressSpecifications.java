package com.turan.librarymanagement.sps;

import com.turan.librarymanagement.domain.Address;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecifications {
    //Ayni postakoduna sahip adressleri filtreleyecek
    public static Specification<Address> hasPostalCode(String postalCode) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("postalCode"), postalCode);
        };
    }
}
