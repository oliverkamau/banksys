package com.link.banksys.accounts.model;

import com.link.banksys.users.model.BankUsers;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name="banksys_accounts")
public class AccountTrans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private Long accountId;

    @Column(name="account_name")
    private String accountName;

    @Column(name="account_type")
    private String accountType;

    @Column(name="account_number")
    private String accountNumber;

    @Column(name="account_balance")
    private BigDecimal accountBalance;

    @Column(name="account_status")
    private String accountStatus;

    @Column(name="account_dateCreated")
    private Date createdAt;

    @Transient
    private String dateCreated;

    @Column(name="account_main")
    private String mainAccount;

    @ManyToOne
    @JoinColumn(name = "account_owner")
    private BankUsers accountOwner;

    @Lob
    @Column(name="account_description")
    private String description;


}
