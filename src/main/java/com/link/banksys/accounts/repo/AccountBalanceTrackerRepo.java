package com.link.banksys.accounts.repo;

import com.link.banksys.accounts.model.AccountBalanceTracker;
import com.link.banksys.accounts.model.AccountTrans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountBalanceTrackerRepo extends JpaRepository<AccountBalanceTracker,Long> {
    AccountBalanceTracker findByAccountBalanceId(Long code);


    List<AccountBalanceTracker> findByAccountTransAndAccountTransType(AccountTrans a, String deposit);

    List<AccountBalanceTracker> findByAccountTrans(AccountTrans accounts);
}
