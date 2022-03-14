package com.link.banksys.users.repo;

import com.link.banksys.users.model.BankUsers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<BankUsers,Long> {
    Long countByUsername(String username);

    BankUsers findByUsername(String username);

    BankUsers findByUserId(Long userId);

    BankUsers findByEmail(String email);

    BankUsers findByUsernameAndEnabled(String username, String s);

    BankUsers findByEmailAndEnabled(String email, String s);
}
