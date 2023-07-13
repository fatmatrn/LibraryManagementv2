package com.turan.librarymanagement.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_borrowers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Email
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true,nullable = false)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "t_borrower_role",
            joinColumns = @JoinColumn(name = "borrower_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles =new HashSet<>();

}