package com.turan.librarymanagement.controller;

import com.turan.librarymanagement.dto.BorrowerDTO;
import com.turan.librarymanagement.service.BorrowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/borrowers")
public class BorrowerController {
    private final BorrowerService borrowerService;
    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BorrowerDTO> getBorrower(@Valid @PathVariable String email){
        BorrowerDTO borrowerDTO =borrowerService.getBorrower(email);
        return ResponseEntity.ok(borrowerDTO);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeBorrower(@Valid @PathVariable String email){
        String message =borrowerService.removeBorrower(email);
        return ResponseEntity.ok(message);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers(){
        List<BorrowerDTO> borrowerDTOList =borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowerDTOList);
    }
}
