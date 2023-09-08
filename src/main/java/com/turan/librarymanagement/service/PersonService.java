package com.turan.librarymanagement.service;

import com.turan.librarymanagement.domain.Address;
import com.turan.librarymanagement.domain.Person;
import com.turan.librarymanagement.repository.AddressRepo;
import com.turan.librarymanagement.repository.PersonRepo;
import com.turan.librarymanagement.sps.AddressSpecifications;
import com.turan.librarymanagement.sps.PersonSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.validation.constraints.FutureOrPresent;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    private PersonRepo personRepo;
    private AddressRepo addressRepo;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Person> findPeopleInCityWithPostalCode(String city, String postalCode) {
        Specification<Person> spec =Specification.where(PersonSpecifications.hasAddressInCityAndPostalCode(city, postalCode));
        return personRepo.findAll(spec);//where yerine not methodu nu denedim calisti
    }


    public List<Person> findPeopleInCityWithPostalCode2(String city, String postalCode) {
        Specification<Person> specCity = (root, query, criteriaBuilder) -> {
            //Subquery<Person> subquery = query.subquery(Person.class);
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Address> subRoot = subquery.from(Address.class);
            subquery.select(subRoot.get("person")).where(criteriaBuilder.equal(subRoot.get("city"), city));

            return criteriaBuilder.in(root.get("id")).value(subquery);

            //SELECT  p FROM Person p
            //JOIN p.addresses a WHERE a.city = :city
        };

        Specification<Person> specPostalCode = (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Address> subRoot = subquery.from(Address.class);
            subquery.select(subRoot.get("person")).where(criteriaBuilder.equal(subRoot.get("postalCode"), postalCode));

            return criteriaBuilder.in(root.get("id")).value(subquery);//value(subquery) ifadesi, in ifadesine alt sorgunun sonucunu ekler.
            /*
            Bu satırda, ana sorgunun sonucu olarak kullanılacak ifadeyi oluşturuyoruz. root.get("id"),
             ana sorgunun id alanını temsil ederken, criteriaBuilder.in() ile alt sorgu sonucunu
              bu id alanı ile karşılaştırıyoruz.
            */
        };

        Specification<Person> spec = Specification.where(specCity.or(specPostalCode));
        Sort sort = Sort.by("id").ascending();
        List<Person> people = personRepo.findAll(spec, sort);

        return people;
    }

//sadece Criteria kullanildi
    public List<Person> getPeopleByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);

        query.select(root).where(criteriaBuilder.equal(root.get("name"), name));

        TypedQuery<Person> typedQuery = entityManager.createQuery(query);
        List<Person> people = typedQuery.getResultList();

        return people;
    }

    public List<Person> findPeopleInCityWithPostalCode4(String city, String postalCode) {
        Specification<Person> spec = Specification.where(PersonSpecifications.hasCity(city).
                or(PersonSpecifications.hasPostalCode(postalCode)));
        return personRepo.findAll(spec);
    }

    public List<Person> findPeopleInCityWithPostalCode5(String city, String postalCode) {
        // İlgili city'ye sahip olan kişileri bulan spesifikasyonu oluşturun
        Specification<Person> specPerson = PersonSpecifications.hasCity2(city);

        // İlgili postalCode'a sahip olan adresleri bulan spesifikasyonu oluşturun
        Specification<Address> specAddress = AddressSpecifications.hasPostalCode(postalCode);

        // İlk olarak, ilgili postalCode'a sahip olan adresleri bulun
        List<Address> addressesWithPostalCode = addressRepo.findAll(specAddress);


        // Ardından, bu adreslerin kişilere ait olduğu kişileri bulun
        List<Long> personIds = addressesWithPostalCode.stream()
                .map(address->address.getPerson().getId())
                .collect(Collectors.toList());

        // Kişileri, ilgili city'ye sahip olanlar arasından seçin
        List<Person> peopleInCityWithPostalCode = personRepo.findAll(specPerson.and(PersonSpecifications.hasIds(personIds)));
        return peopleInCityWithPostalCode;
    }

//    public List<Person> findPeopleInCityWithPostalCode2(String city, String postalCode) {
//
//        Specification<Person> specCity = new Specification<Person>() {
//
//
//            @Override
//            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get("addresses").get("city"), city);
//            }
//
//        };
//
//       Specification<Person> specPostalCode = new Specification<Person>() {
//
//            @Override
//            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.greaterThanOrEqualTo(root.get("addresses").get("postalCode"), postalCode);
//            }
//
//        };
//
//        Specification<Person> spec =Specification.where(specCity.or(specPostalCode));
//        Sort sort = Sort.by("id").ascending();
//          List<Person> people = personRepo.findAll(spec,sort);
//
//          return people;
//
//
//    }

}


