package com.link.banksys.users.service;

import com.link.banksys.exceptions.BadRequestException;
import com.link.banksys.users.model.BankUsers;

public interface UserService {
    BankUsers getUser(Long userId);

    void updateProfile(BankUsers bankUsers);

    Long countByUsername(String username);

    BankUsers findByEmail(String email);

    void save(BankUsers bankUsers, String baseUrl) throws BadRequestException;

    void activateUser(Long userId);

}
