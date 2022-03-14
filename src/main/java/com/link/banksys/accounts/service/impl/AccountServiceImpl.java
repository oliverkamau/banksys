package com.link.banksys.accounts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.link.banksys.accounts.model.*;
import com.link.banksys.accounts.repo.AccountBalanceTrackerRepo;
import com.link.banksys.accounts.repo.AccountsRepo;
import com.link.banksys.accounts.service.AccountTransService;
import com.link.banksys.exceptions.BadRequestException;
import com.link.banksys.users.model.BankUsers;
import com.link.banksys.utils.UserUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountTransService {

    private AccountsRepo accountsRepo;
    private UserUtils userUtils;
    private AccountBalanceTrackerRepo balanceTrackerRepo;
    private JavaMailSender javaMailSender;

    public AccountServiceImpl(AccountsRepo accountsRepo, UserUtils userUtils, AccountBalanceTrackerRepo balanceTrackerRepo, JavaMailSender javaMailSender) {
        this.accountsRepo = accountsRepo;
        this.userUtils = userUtils;
        this.balanceTrackerRepo = balanceTrackerRepo;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public AccountTrans createAccount(AccountTrans account) {

        if(account.getAccountId()==null) {

            String template = "0";
            Date d = new Date();
            account.setCreatedAt(d);
            account.setAccountStatus("Active");
            account.setAccountBalance(BigDecimal.ZERO);
            account.setAccountOwner(userUtils.getCurrentUser());
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH);
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            AccountTrans acc = accountsRepo.save(account);
            acc.setAccountNumber(template + acc.getAccountId() + day + month + year);
            accountsRepo.save(acc);
            String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(acc.getCreatedAt());
            acc.setDateCreated(cDate);
            AccountBalanceTracker balanceTracker = new AccountBalanceTracker();
            balanceTracker.setAccountTrans(acc);
            balanceTracker.setAccountTransType("Account Creation");
            balanceTracker.setPreTransBalance(BigDecimal.ZERO);
            balanceTracker.setPostTransBalance(BigDecimal.ZERO);
            balanceTracker.setAmountTranscated(BigDecimal.ZERO);
            balanceTracker.setRemarks("Opened a new Account "+acc.getAccountNumber());
            balanceTracker.setTransactionCode("ACC"+acc.getAccountNumber());
            balanceTracker.setTransactedBy(userUtils.getCurrentUser());
            balanceTracker.setTransactionDate(new Date());
            balanceTrackerRepo.save(balanceTracker);
            return acc;

        }else{
            AccountTrans acc = accountsRepo.findByAccountId(account.getAccountId());
            LocalDateTime now = LocalDateTime.now();

            acc.setAccountType(account.getAccountType());
            acc.setAccountName(account.getAccountName());
            acc.setDescription(account.getDescription());
            AccountBalanceTracker balanceTracker = new AccountBalanceTracker();
            balanceTracker.setAccountTrans(acc);
            balanceTracker.setAccountTransType("Account Update");
            balanceTracker.setPreTransBalance(BigDecimal.ZERO);
            balanceTracker.setPostTransBalance(BigDecimal.ZERO);
            balanceTracker.setAmountTranscated(BigDecimal.ZERO);
            balanceTracker.setRemarks("Updated Account "+acc.getAccountNumber());
            balanceTracker.setTransactionCode("ACC"+acc.getAccountNumber()+now.getSecond());
            balanceTracker.setTransactedBy(userUtils.getCurrentUser());
            balanceTracker.setTransactionDate(new Date());
            accountsRepo.save(acc);
            balanceTrackerRepo.save(balanceTracker);

            return acc;
        }


    }

    @Override
    public List<AccountTrans> getAccounts() {
        BankUsers bankUsers = userUtils.getCurrentUser();
        List<AccountTrans> accounts = accountsRepo.findByAccountOwnerAndAccountStatus(bankUsers,"Active");
        for(AccountTrans a: accounts){

            String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(a.getCreatedAt());
            a.setDateCreated(cDate);

        }
        return accounts;
    }

    @Override
    public AccountTrans editAccount(Long code) {
        AccountTrans acc = accountsRepo.findByAccountId(code);
        String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(acc.getCreatedAt());
        acc.setDateCreated(cDate);
        return acc;
    }

    @Override
    public JSONObject geteditfloat(Long code) {
        AccountBalanceTracker tracker = balanceTrackerRepo.findByAccountBalanceId(code);
        JSONObject json = new JSONObject();
        json.put("code",tracker.getAccountBalanceId());
        json.put("accountCode",tracker.getAccountTrans().getAccountId());
        json.put("accountName",tracker.getAccountTrans().getAccountName());
        json.put("amount",tracker.getAmountTranscated());
        json.put("balance",tracker.getPostTransBalance());
        json.put("wording",tracker.getWording());
        String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(tracker.getTransactionDate());
        json.put("date",cDate);
        return json;
    }

    @Override
    public List<JSONObject> getdepositsgrid() {
        BankUsers bankUsers = userUtils.getCurrentUser();
        List<AccountTrans> accounts = accountsRepo.findByAccountOwnerAndAccountStatus(bankUsers,"Active");
        List<JSONObject> jsons = new ArrayList<>();
        for(AccountTrans a: accounts){

            List<AccountBalanceTracker> trackers = balanceTrackerRepo.findByAccountTransAndAccountTransType(a,"Deposit");
            for(AccountBalanceTracker t: trackers){
                JSONObject json = new JSONObject();
                json.put("code",t.getAccountBalanceId());
                json.put("transtype",t.getAccountTransType());
                json.put("account",t.getAccountTrans().getAccountNumber());
                json.put("amount",t.getAmountTranscated());
                json.put("balance",t.getPostTransBalance());
                json.put("transcode",t.getTransactionCode());
                String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getTransactionDate());
                json.put("transdate",cDate);
                jsons.add(json);
            }

        }
        return jsons;
    }
    @Override
    public List<JSONObject> getwithdrawgrid() {
        BankUsers bankUsers = userUtils.getCurrentUser();
        List<AccountTrans> accounts = accountsRepo.findByAccountOwnerAndAccountStatus(bankUsers,"Active");
        List<JSONObject> jsons = new ArrayList<>();
        for(AccountTrans a: accounts){

            List<AccountBalanceTracker> trackers = balanceTrackerRepo.findByAccountTransAndAccountTransType(a,"Withdraw");
            for(AccountBalanceTracker t: trackers){
                JSONObject json = new JSONObject();
                json.put("code",t.getAccountBalanceId());
                json.put("transtype",t.getAccountTransType());
                json.put("account",t.getAccountTrans().getAccountNumber());
                json.put("amount",t.getAmountTranscated());
                json.put("balance",t.getPostTransBalance());
                json.put("transcode",t.getTransactionCode());
                String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getTransactionDate());
                json.put("transdate",cDate);
                jsons.add(json);
            }

        }
        return jsons;
    }

    @Override
    public List<JSONObject> gettransferdrawgrid() {
        BankUsers bankUsers = userUtils.getCurrentUser();
        List<AccountTrans> accounts = accountsRepo.findByAccountOwnerAndAccountStatus(bankUsers,"Active");
        List<JSONObject> jsons = new ArrayList<>();
        for(AccountTrans a: accounts){

            List<AccountBalanceTracker> trackers = balanceTrackerRepo.findByAccountTransAndAccountTransType(a,"Transfer");
            for(AccountBalanceTracker t: trackers){
                JSONObject json = new JSONObject();
                json.put("code",t.getAccountBalanceId());
                json.put("transtype",t.getAccountTransType());
                json.put("fromaccount",t.getAccountTrans().getAccountNumber());
                json.put("toaccount",t.getNewAccountTrans().getAccountNumber());
                json.put("amount",t.getAmountTranscated());
                json.put("tobalance",t.getPostTransBalance());

                json.put("transcode",t.getTransactionCode());
                String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getTransactionDate());
                json.put("transdate",cDate);
                jsons.add(json);
            }

        }
        return jsons;
    }

    @Override
    public JSONObject depositMoney(TransactionBean jsonObject) {

        JSONObject json = new JSONObject();
        AccountBalanceTracker tracker = new AccountBalanceTracker();
        AccountTrans acc = accountsRepo.findByAccountId(jsonObject.getDepositAccount());
        String a = jsonObject.getAmount();
        BigDecimal amount = new BigDecimal(a);
        tracker.setAccountTrans(acc);
        tracker.setPreTransBalance(acc.getAccountBalance());
        tracker.setTransactedBy(userUtils.getCurrentUser());
        tracker.setTransactionDate(new Date());
        tracker.setWording(jsonObject.getWording());
        tracker.setAccountTransType("Deposit");
        tracker.setAmountTranscated(amount);
        tracker.setPostTransBalance(amount.add(acc.getAccountBalance()));
        AccountBalanceTracker saved = balanceTrackerRepo.save(tracker);
        saved.setTransactionCode("TRN00"+userUtils.getCurrentUser().getUserId()+saved.getAccountBalanceId());
        balanceTrackerRepo.save(saved);
        acc.setAccountBalance(amount.add(acc.getAccountBalance()));
        accountsRepo.save(acc);
        String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(saved.getTransactionDate());
        json.put("date",cDate);
        json.put("success", saved.getWording()+" Deposited Successfully in Account "+acc.getAccountNumber());
        json.put("transactionId",saved.getAccountBalanceId());
        return json;
    }

    @Override
    public JSONObject withDrawMoney(TransactionBean jsonObject) throws BadRequestException {

        JSONObject json = new JSONObject();
        AccountBalanceTracker tracker = new AccountBalanceTracker();
        AccountTrans acc = accountsRepo.findByAccountId(jsonObject.getWithDrawAccount());
        String a = jsonObject.getAmount();
        BigDecimal amount = new BigDecimal(a);

        int c = acc.getAccountBalance().compareTo(amount);
        if(c < 0){
            throw new BadRequestException("Sorry! Sh. "+amount+" is greater than your current account balance of "+acc.getAccountBalance());
        }
        tracker.setAccountTrans(acc);
        tracker.setPreTransBalance(acc.getAccountBalance());
        tracker.setTransactedBy(userUtils.getCurrentUser());
        tracker.setTransactionDate(new Date());
        tracker.setWording(jsonObject.getWording());
        tracker.setAccountTransType("Withdraw");
        tracker.setAmountTranscated(amount);
        tracker.setRemarks(jsonObject.getRemarks());
        tracker.setPostTransBalance(acc.getAccountBalance().subtract(amount));
        AccountBalanceTracker saved = balanceTrackerRepo.save(tracker);
        saved.setTransactionCode("TRN00"+userUtils.getCurrentUser().getUserId()+saved.getAccountBalanceId());
        balanceTrackerRepo.save(saved);
        acc.setAccountBalance(acc.getAccountBalance().subtract(amount));
        accountsRepo.save(acc);
        String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(saved.getTransactionDate());
        json.put("date",cDate);
        json.put("success", saved.getWording()+" Withdrawn Successfully from Account "+acc.getAccountNumber());
        json.put("transactionId",saved.getAccountBalanceId());
        return json;
    }

    @Override
    public JSONObject transferMoney(TransferBean jsonObject) throws BadRequestException {
        JSONObject json = new JSONObject();
        AccountBalanceTracker tracker = new AccountBalanceTracker();
        AccountTrans acc = accountsRepo.findByAccountId(jsonObject.getDebitAccount());
        AccountTrans acc2 = accountsRepo.findByAccountId(jsonObject.getCreditAccount());

        String a = jsonObject.getAmount();
        BigDecimal amount = new BigDecimal(a);

        int c = acc.getAccountBalance().compareTo(amount);
        if(c < 0){
            throw new BadRequestException("Sorry! Sh. "+amount+" is greater than your current account balance of "+acc.getAccountBalance());
        }
        tracker.setAccountTrans(acc);
        tracker.setPreTransBalance(acc.getAccountBalance());
        tracker.setTransactedBy(userUtils.getCurrentUser());
        tracker.setTransactionDate(new Date());
        tracker.setWording(jsonObject.getWording());
        tracker.setAccountTransType("Transfer");
        tracker.setAmountTranscated(amount);
        tracker.setPostTransBalance(acc.getAccountBalance().add(amount));
        tracker.setNewAccountTrans(acc2);
        AccountBalanceTracker saved = balanceTrackerRepo.save(tracker);
        saved.setTransactionCode("TRN00"+userUtils.getCurrentUser().getUserId()+saved.getAccountBalanceId());
        balanceTrackerRepo.save(saved);
        acc.setAccountBalance(acc.getAccountBalance().subtract(amount));
        acc2.setAccountBalance(acc2.getAccountBalance().add(amount));
        accountsRepo.save(acc);
        accountsRepo.save(acc2);
        String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(saved.getTransactionDate());
        json.put("date",cDate);
        json.put("success", saved.getWording()+" Transfered Successfully from Account "+acc.getAccountNumber()+" to account "+acc2.getAccountNumber());
        json.put("transactionId",saved.getAccountBalanceId());
        return json;
    }

    @Override
    public void sendMail(Long code) throws MessagingException, UnsupportedEncodingException {

        AccountBalanceTracker tracker = balanceTrackerRepo.findByAccountBalanceId(code);


        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("kamaugaby13@gmail.com","BANKSYS");
        helper.setTo(userUtils.getCurrentUser().getEmail());

        helper.setSubject("ACCOUNT TRANSACTION INFORMATION UPDATE");
        String mail = "<h2>"+tracker.getAccountTransType().toUpperCase()+" of Ksh. "+tracker.getAmountTranscated()+"</h2><br> " +
                "<h3> This is to confirm "+tracker.getAccountTransType().toUpperCase()+" of Ksh. "+tracker.getAmountTranscated()+ " in your account "+tracker.getAccountTrans().getAccountNumber()+"</h3>";
        message.setContent(mail,"text/html; charset=utf-8");
        javaMailSender.send(message);


    }

    @Override
    public List<Select2Bean> searchaccount(String query) {
        List<AccountTrans> accountTrans = accountsRepo.accounts(query);
        List<Select2Bean> jsonObjects = new ArrayList<>();
        for(AccountTrans acc: accountTrans){
            Select2Bean jsonObject = new Select2Bean();
            jsonObject.setId(acc.getAccountId().toString());
            jsonObject.setText(acc.getAccountNumber()+"---"+acc.getAccountType());
            jsonObjects.add(jsonObject);
        }
        return jsonObjects;
    }

    @Override
    public JSONObject balance(Long code) {
        AccountTrans acc = accountsRepo.findByAccountId(code);
        JSONObject json = new JSONObject();
        json.put("balance",acc.getAccountBalance());
        return json;
    }

    @Override
    public List<JSONObject> getalltransactions(Long code) {
        AccountTrans accounts = accountsRepo.findByAccountId(code);
        List<JSONObject> jsons = new ArrayList<>();
        List<AccountBalanceTracker> trackers = balanceTrackerRepo.findByAccountTrans(accounts);
        for(AccountBalanceTracker t: trackers){
            JSONObject json = new JSONObject();
            json.put("code",t.getTransactionCode());
            json.put("transtype",t.getAccountTransType());
            json.put("account",t.getAccountTrans().getAccountNumber());
            json.put("amount",t.getAmountTranscated());
            json.put("balance",t.getPostTransBalance());
            json.put("transcode",t.getTransactionCode());
            String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getTransactionDate());
            json.put("transdate",cDate);
            jsons.add(json);


        }
        return jsons;
    }

    @Override
    public List<JSONObject> getspecifictransactions(Long code, String transaction) {
        AccountTrans accounts = accountsRepo.findByAccountId(code);
        List<JSONObject> jsons = new ArrayList<>();
        if(transaction.equalsIgnoreCase("Other")){
            transaction = "Transfer";
        }
        List<AccountBalanceTracker> trackers = balanceTrackerRepo.findByAccountTransAndAccountTransType(accounts,transaction);
            for(AccountBalanceTracker t: trackers){
                JSONObject json = new JSONObject();
                json.put("code",t.getTransactionCode());
                json.put("transtype",t.getAccountTransType());
                json.put("account",t.getAccountTrans().getAccountNumber());
                json.put("amount",t.getAmountTranscated());
                json.put("balance",t.getPostTransBalance());
                json.put("transcode",t.getTransactionCode());
                String cDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getTransactionDate());
                json.put("transdate",cDate);
                jsons.add(json);


        }
        return jsons;
    }
}
