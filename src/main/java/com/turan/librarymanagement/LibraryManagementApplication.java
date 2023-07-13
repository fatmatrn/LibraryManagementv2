package com.turan.librarymanagement;

import com.turan.librarymanagement.domain.Role;
import com.turan.librarymanagement.domain.enums.RoleType;
import com.turan.librarymanagement.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }
}

@Component
@AllArgsConstructor
class DemoCommandLineRunner implements CommandLineRunner {

    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        if (!roleRepository.findByType(RoleType.ROLE_MEMBER).isPresent()) {
            Role roleCustomer = new Role();
            roleCustomer.setType(RoleType.ROLE_MEMBER);
            roleRepository.save(roleCustomer);
        }

        if (!roleRepository.findByType(RoleType.ROLE_ADMIN).isPresent()) {
            Role roleAdmin = new Role();
            roleAdmin.setType(RoleType.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }
    }

}
