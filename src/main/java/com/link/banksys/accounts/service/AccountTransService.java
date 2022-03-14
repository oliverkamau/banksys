package com.link.banksys.accounts.service;

import com.alibaba.fastjson.JSONObject;
import com.link.banksys.accounts.model.AccountTrans;
import com.link.banksys.accounts.model.Select2Bean;
import com.link.banksys.accounts.model.TransactionBean;
import com.link.banksys.accounts.model.TransferBean;
import com.link.banksys.exceptions.BadRequestException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AccountTransService {
    AccountTrans createAccount(AccountTrans account);

    List<AccountTrans> getAccounts();

    AccountTrans editAccount(Long code);

    JSONObject geteditfloat(Long code);

    List<JSONObject> getdepositsgrid();

    JSONObject depositMoney(TransactionBean jsonObject);

    void sendMail(Long code) throws MessagingException, UnsupportedEncodingException;

    List<Select2Bean> searchaccount(String query);

    JSONObject balance(Long code);

    JSONObject withDrawMoney(TransactionBean jsonObject) throws BadRequestException;

    List<JSONObject> getwithdrawgrid();

    List<JSONObject> getalltransactions(Long code);

    List<JSONObject> getspecifictransactions(Long code, String transaction);

    List<JSONObject> gettransferdrawgrid();

    JSONObject transferMoney(TransferBean jsonObject) throws BadRequestException;
}
