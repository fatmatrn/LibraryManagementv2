package com.turan.librarymanagement.service;



import com.turan.librarymanagement.domain.Role;
import com.turan.librarymanagement.domain.enums.RoleType;
import com.turan.librarymanagement.exception.ErrorMessage;
import com.turan.librarymanagement.exception.ResourceNotFoundException;
import com.turan.librarymanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByType(RoleType roleType) {
    Role role= roleRepository.findByType(roleType).orElseThrow(
                ()->new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION,roleType))
        );
        return  role;
    }
}
