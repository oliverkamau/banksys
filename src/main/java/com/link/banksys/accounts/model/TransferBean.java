package com.link.banksys.accounts.model;

import lombok.Data;

@Data
public class TransferBean {

    private String amount;
    private Long debitAccount;
    private Long creditAccount;
    private String wording;


}
