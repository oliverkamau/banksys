package com.link.banksys.accounts.controller;

import com.alibaba.fastjson.JSONObject;
import com.link.banksys.accounts.model.*;
import com.link.banksys.accounts.service.AccountTransService;
import com.link.banksys.exceptions.BadRequestException;
import com.link.banksys.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/protected/accounts")
public class AccountController {

    private UserUtils userUtils;
    private AccountTransService accountTransService;

    public AccountController(UserUtils userUtils, AccountTransService accountTransService) {
        this.userUtils = userUtils;
        this.accountTransService = accountTransService;
    }

    @PostMapping("/createAccount")
    @ResponseBody
    public AccountTrans registerUser(AccountTrans account) throws BadRequestException {
        return accountTransService.createAccount(account);
    }
    @GetMapping("/getAccounts")
    @ResponseBody
    public List<AccountTrans> getAccounts() throws BadRequestException {
        return accountTransService.getAccounts();
    }
    @GetMapping("/newaccount")
    public String register(Model model){

        model.addAttribute("username",userUtils.getCurrentUser().getUsername());
        return "pages/accounts/openaccount.html";

    }
    @GetMapping("/depositscreen")
    public String depositscreen(Model model){

        model.addAttribute("username",userUtils.getCurrentUser().getUsername());
        return "pages/accounts/deposit.html";

    }

    @GetMapping("/withdrawscreen")
    public String withdrawscreen(Model model){

        model.addAttribute("username",userUtils.getCurrentUser().getUsername());
        return "pages/accounts/withdraw.html";

    }
    @GetMapping("/transferscreen")
    public String transferscreen(Model model){

        model.addAttribute("username",userUtils.getCurrentUser().getUsername());
        return "pages/accounts/transfer.html";

    }
    @GetMapping("/transactionhistory")
    public String transactionhistory(Model model){

        model.addAttribute("username",userUtils.getCurrentUser().getUsername());
        return "pages/accounts/transactionhistory.html";

    }
    @GetMapping("/editAccount/{code}")
    @ResponseBody
    public AccountTrans editAccount(@PathVariable Long code) throws BadRequestException {
        return accountTransService.editAccount(code);
    }

    @GetMapping("/geteditfloat/{code}")
    @ResponseBody
    public JSONObject geteditfloat(@PathVariable Long code) throws BadRequestException {
        return accountTransService.geteditfloat(code);
    }
    @GetMapping("/getdepositsgrid")
    @ResponseBody
    public List<JSONObject> getdepositsgrid() throws BadRequestException {
        return accountTransService.getdepositsgrid();
    }
    @GetMapping("/gettransferdrawgrid")
    @ResponseBody
    public List<JSONObject> gettransferdrawgrid() throws BadRequestException {
        return accountTransService.gettransferdrawgrid();
    }
    @GetMapping("/getwithdrawgrid")
    @ResponseBody
    public List<JSONObject> getwithdrawgrid() throws BadRequestException {
        return accountTransService.getwithdrawgrid();
    }
    @GetMapping("/getalltransactions/{code}")
    @ResponseBody
    public List<JSONObject> getalltransactions(@PathVariable Long code) throws BadRequestException {
        return accountTransService.getalltransactions(code);
    }
    @GetMapping("/getspecifictransactions/{code}/{transaction}")
    @ResponseBody
    public List<JSONObject> getspecifictransactions(@PathVariable Long code,@PathVariable String transaction) throws BadRequestException {
        return accountTransService.getspecifictransactions(code,transaction);
    }
    @PostMapping("/depositMoney")
    @ResponseBody
    public JSONObject depositMoney(TransactionBean jsonObject) throws BadRequestException {
        return accountTransService.depositMoney(jsonObject);
    }
    @PostMapping("/withDrawMoney")
    @ResponseBody
    public JSONObject withDrawMoney(TransactionBean jsonObject) throws BadRequestException {
        return accountTransService.withDrawMoney(jsonObject);
    }
    @PostMapping("/transferMoney")
    @ResponseBody
    public JSONObject transferMoney(TransferBean jsonObject) throws BadRequestException {
        return accountTransService.transferMoney(jsonObject);
    }
    @GetMapping("/sendMail/{code}")
    @ResponseBody
    public void sendMail(@PathVariable Long code) throws UnsupportedEncodingException, MessagingException {
       accountTransService.sendMail(code);
    }
    @GetMapping("/getcreditbalance/{code}")
    @ResponseBody
    public JSONObject getcreditbalance(@PathVariable Long code) throws UnsupportedEncodingException, MessagingException {
        return accountTransService.balance(code);
    }
    @GetMapping("/getdebitbalance/{code}")
    @ResponseBody
    public JSONObject getdebitbalance(@PathVariable Long code) throws UnsupportedEncodingException, MessagingException {
        return accountTransService.balance(code);
    }


    @GetMapping("/balance/{code}")
    @ResponseBody
    public JSONObject balance(@PathVariable Long code) throws UnsupportedEncodingException, MessagingException {
        return accountTransService.balance(code);
    }
    @GetMapping("/searchaccount")
    @ResponseBody
    public WrapperSelect searchaccount(@RequestParam(required=false) String query) throws BadRequestException {
        WrapperSelect json = new WrapperSelect();

        String search = "";
        if(query==null||query.isEmpty()){
            query = search;
        }
        List<Select2Bean> jsons = accountTransService.searchaccount(query);
        json.setResults(jsons);

        return json;

    }
    @GetMapping("/searchCreditaccount")
    @ResponseBody
    public WrapperSelect searchCreditaccount(@RequestParam(required=false) String query) throws BadRequestException {
        WrapperSelect json = new WrapperSelect();

        String search = "";
        if(query==null||query.isEmpty()){
            query = search;
        }
        List<Select2Bean> jsons = accountTransService.searchaccount(query);
        json.setResults(jsons);

        return json;

    }
    @GetMapping("/searchDebitaccount")
    @ResponseBody
    public WrapperSelect searchDebitaccount(@RequestParam(required=false) String query) throws BadRequestException {
        WrapperSelect json = new WrapperSelect();

        String search = "";
        if(query==null||query.isEmpty()){
            query = search;
        }
        List<Select2Bean> jsons = accountTransService.searchaccount(query);
        json.setResults(jsons);

        return json;

    }

}
