package com.link.banksys.accounts.model;

import com.link.banksys.users.model.BankUsers;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name="banksys_accountbalance_tracker")
public class AccountBalanceTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private Long accountBalanceId;

    @ManyToOne
    @JoinColumn(name = "accountbalance_account")
    private AccountTrans accountTrans;

    @Column(name="accounttransaction_type")
    private String accountTransType;

    @Column(name="accountpretrans_balance")
    private BigDecimal preTransBalance;

    @Column(name="accountposttrans_balance")
    private BigDecimal postTransBalance;

    @Column(name="accountamount_transacted")
    private BigDecimal amountTranscated;

    @Column(name="transaction_date")
    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "transacted_by")
    private BankUsers transactedBy;

    @Column(name="transaction_code")
    private String transactionCode;

    @Lob
    @Column(name="transaction_remarks")
    private String remarks;

    @Lob
    @Column(name="transaction_wording")
    private String wording;

    @ManyToOne
    @JoinColumn(name = "accountbalance_new_account")
    private AccountTrans newAccountTrans;


}
