package com.turan.librarymanagement.sps;

import com.turan.librarymanagement.domain.Address;
import com.turan.librarymanagement.domain.Person;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.util.List;

public class PersonSpecifications {

    public static Specification<Person> hasAddressInCityAndPostalCode(String city, String postalCode) {
        return (root, query, criteriaBuilder) -> {
            Join<Person, Address> addressJoin = root.join("addresses", JoinType.INNER);
            Predicate cityPredicate = criteriaBuilder.equal(addressJoin.get("city"), city);
            Predicate postalCodePredicate = criteriaBuilder.equal(addressJoin.get("postalCode"), postalCode);


            return criteriaBuilder.and(cityPredicate, postalCodePredicate);
        };
    }
    public static Specification<Person> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Address> subRoot = subquery.from(Address.class);
            subquery.select(subRoot.get("person")).where(criteriaBuilder.equal(subRoot.get("city"), city));

            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }

    public static Specification<Person> hasPostalCode(String postalCode) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Address> subRoot = subquery.from(Address.class);
            subquery.select(subRoot.get("person")).where(criteriaBuilder.equal(subRoot.get("postalCode"), postalCode));

            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }
    //Ayni city ye sahip personlari filtreleyecek
    public static Specification<Person> hasCity2(String city) {
        return (root, query, criteriaBuilder) -> {
            Join<Person, Address> addressJoin = root.join("addresses");
            return criteriaBuilder.equal(addressJoin.get("city"), city);
        };
    }

    public static Specification<Person> hasIds(List<Long> personIds) {
        return (root, query, criteriaBuilder) -> {
            if (personIds != null && !personIds.isEmpty()) {
                return root.get("id").in(personIds);
            } else {
                // Eğer personIds boşsa veya null ise herhangi bir filtre uygulamayın
                return criteriaBuilder.conjunction();
            }
        };}

//    Specification<Sale> personSpec = new Specification<Sale>()
//    {
//        private static final long serialVersionUID = -8354621229900183586L;
//
//        @Override
//        public Predicate toPredicate(Root<Sale> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
//        {
//            return criteriaBuilder.equal(root.get("person").get("name"), name);
//        }
//    };
}
//Specification.where(null) kullanılarak başlatılır. Bu başlangıçta hiçbir filtreleme kriteri içermeyen bir Specification nesnesini temsil eder.