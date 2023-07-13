package com.turan.librarymanagement.service;

import com.turan.librarymanagement.domain.Borrower;
import com.turan.librarymanagement.domain.Role;
import com.turan.librarymanagement.domain.enums.RoleType;
import com.turan.librarymanagement.dto.BorrowerDTO;
import com.turan.librarymanagement.exception.BadRequestException;
import com.turan.librarymanagement.exception.ErrorMessage;
import com.turan.librarymanagement.exception.ResourceNotFoundException;
import com.turan.librarymanagement.exception.ResponseMessage;
import com.turan.librarymanagement.repository.BorrowerRepository;
import com.turan.librarymanagement.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    public BorrowerService(BorrowerRepository borrowerRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder) {
        this.borrowerRepository = borrowerRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }
    public BorrowerDTO createBorrower(BorrowerDTO borrowerDTO) {
            boolean email=borrowerRepository.existsByEmail(borrowerDTO.getEmail());
            boolean phone=borrowerRepository.existsByPhoneNumber(borrowerDTO.getPhoneNumber());
            if (email||phone){
                throw new BadRequestException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,email));
            }
            Borrower borrower=borrowerDTOToBorrower(borrowerDTO);
            borrowerRepository.save(borrower);
            return borrowerDTO;
    }
    public BorrowerDTO getBorrower(String email) {
        Borrower borrower =callBorrower(email);
        return borrowerToBorrowerDTO(borrower);
    }
    public String removeBorrower(String email) {
        Borrower borrower=callBorrower(email);
        borrowerRepository.delete(borrower);
        return ResponseMessage.BORROWER_DELETE_MESSAGE_RESPONSE;
    }

    public List<BorrowerDTO> getAllBorrowers() {
        List<Borrower> borrowerList =borrowerRepository.findAll();
        List<BorrowerDTO> borrowerDTOList =new ArrayList<>();
        for (Borrower borrower :borrowerList){
            borrowerDTOList.add(borrowerToBorrowerDTO(borrower));
        }
        return borrowerDTOList;
    }

    //convert methods
    private Borrower borrowerDTOToBorrower(BorrowerDTO borrowerDTO){
       Borrower borrower =new Borrower();
        borrower.setName(borrowerDTO.getName());
        borrower.setEmail(borrowerDTO.getEmail());
        borrower.setPhoneNumber(borrowerDTO.getPhoneNumber());
        Role role = roleService.findByType(RoleType.ROLE_MEMBER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        borrower.setRoles(roles);

        String encodedPassword= passwordEncoder.encode(borrowerDTO.getPassword());
        borrower.setPassword(encodedPassword);
        return borrower;
    }

    // Assist Methods
    private  BorrowerDTO borrowerToBorrowerDTO(Borrower borrower){
        BorrowerDTO borrowerDTO =new BorrowerDTO();
        borrowerDTO.setName(borrower.getName());
        borrowerDTO.setEmail(borrower.getEmail());
        borrowerDTO.setPhoneNumber(borrower.getPhoneNumber());
        borrowerDTO.setPassword(borrower.getPassword());
        return borrowerDTO;
    }

    public Borrower callBorrower(String email){
        Borrower borrower = borrowerRepository.findByEmail(email).orElseThrow(
                ()->new ResourceNotFoundException(ErrorMessage.BORROWER_NOT_FOUND_EXCEPTION)
        );
        return borrower;
    }
    public Borrower getCurrentBorrower() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(
                () ->new ResourceNotFoundException(ErrorMessage.BORROWER_NOT_FOUND_EXCEPTION));
        Borrower borrower = callBorrower(email);
        return borrower;
    }

}
