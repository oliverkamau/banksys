package com.link.banksys.accounts.repo;

import com.link.banksys.accounts.model.AccountTrans;
import com.link.banksys.users.model.BankUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountsRepo extends JpaRepository<AccountTrans,Long> {

    AccountTrans findByAccountId(Long code);

    List<AccountTrans> findByAccountOwnerAndAccountStatus(BankUsers bankUsers, String active);

    @Query("select c from  AccountTrans c where c.accountName like %:query% or c.accountNumber like %:query%")
    List<AccountTrans> accounts(String query);


}
