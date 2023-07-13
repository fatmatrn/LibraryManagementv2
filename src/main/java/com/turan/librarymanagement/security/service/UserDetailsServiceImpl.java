package com.turan.librarymanagement.security.service;


import com.turan.librarymanagement.domain.Borrower;
import com.turan.librarymanagement.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BorrowerService borrowerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Borrower borrower= borrowerService.callBorrower(email);
        return UserDetailsImpl.build(borrower) ;
    }
}
