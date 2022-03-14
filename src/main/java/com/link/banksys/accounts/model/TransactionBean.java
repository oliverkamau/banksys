package com.link.banksys.accounts.model;

import lombok.Data;

@Data
public class TransactionBean {

    private Long depositAccount;
    private Long withDrawAccount;
    private String amount;
    private String wording;
    private String remarks;
}
