package com.turan.librarymanagement.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tbl_books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(unique = true,nullable = false)
    private String isbn;
    @Column(nullable = false)
    private int availableCopies;

}