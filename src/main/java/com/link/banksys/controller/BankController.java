package com.link.banksys.controller;

import com.alibaba.fastjson.JSONObject;
import com.link.banksys.users.model.BankUsers;
import com.link.banksys.users.service.UserService;
import com.link.banksys.utils.UserUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/protected")
public class BankController {

    private UserUtils userUtils;
    private UserService userService;
    private BCryptPasswordEncoder encoder;

    public BankController(UserUtils userUtils, UserService userService, BCryptPasswordEncoder encoder) {
        this.userUtils = userUtils;
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/home")
    public String home(){

        return "pages/home/welcome.html";

    }

    @GetMapping("/profile")
    public String profile(Model model){
        return "pages/users/profile.html";

    }
    @GetMapping("/getuser")
    @ResponseBody
    public BankUsers getUser(){

        BankUsers bankUsers = userUtils.getCurrentUser();
        if(bankUsers.getBirthDate()!=null) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(bankUsers.getBirthDate());
            bankUsers.setDob(date);
        }
        if(bankUsers.getPhoto()!=null){
            bankUsers.setUrl("/protected/getPhoto/"+ bankUsers.getUserId());
        }
        return bankUsers;

    }
    @GetMapping(value = "/getPhoto/{userId}")
    public void getImage(HttpServletResponse response, @PathVariable Long userId)
            throws IOException, ServletException {
        BankUsers bankUsers = userService.getUser(userId);
        if (bankUsers.getPhoto()!=null ) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(bankUsers.getPhoto());
            response.getOutputStream().close();
        }
    }
    @PostMapping("/updateProfile")
    @ResponseBody
    public JSONObject updateProfile(BankUsers bankUsers) throws IOException {
     JSONObject json = new JSONObject();
    if(bankUsers.getFile()!=null){
        bankUsers.setPhoto(bankUsers.getFile().getBytes());
    }
    System.out.println(bankUsers.getDob());
    userService.updateProfile(bankUsers);
    json.put("success","Profile Updated Successfully");
    return json;

    }



}
