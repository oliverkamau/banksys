package com.link.banksys.users.service;

import com.link.banksys.config.UserDetailsImpl;
import com.link.banksys.users.model.BankUsers;
import com.link.banksys.users.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BankSysUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public BankSysUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BankUsers bankUsers = userRepository.findByUsernameAndEnabled(username,"1");
        if(bankUsers ==null) {
            System.out.println("BankUsers is invalid");
            throw new UsernameNotFoundException("Invalid Username or Password");
        }
        return new UserDetailsImpl(bankUsers);
    }
}
