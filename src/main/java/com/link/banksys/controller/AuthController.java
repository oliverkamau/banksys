package com.link.banksys.controller;

import com.alibaba.fastjson.JSONObject;
import com.link.banksys.exceptions.BadRequestException;
import com.link.banksys.users.model.BankUsers;
import com.link.banksys.users.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/login");
        return mv;

//        return "pages/login.html";

    }

    @GetMapping("/loginerror")
    public String login(Model model){

        model.addAttribute("error","Invalid Username or Password");
        return "pages/login.html";

    }
    @GetMapping("/register")
    public String register(){

        return "pages/register.html";

    }
    @PostMapping("/registerUser")
    @ResponseBody
    public JSONObject registerUser(BankUsers bankUsers, HttpServletRequest request) throws BadRequestException {
       String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
       JSONObject json = new JSONObject();
       userService.save(bankUsers,baseUrl);
       json.put("success","BankUsers registered successfully proceed to your email "+ bankUsers.getEmail()+" to activate your account");
       return json;
    }
    @GetMapping("/activate")
    @ResponseBody
    public ModelAndView activateUser(@RequestParam Long userId) throws BadRequestException {

        ModelAndView mv = new ModelAndView();
        userService.activateUser(userId);
        mv.setViewName("pages/login");
        return mv;
    }
//    @GetMapping("/deleteuser")
//    @ResponseBody
//    public JSONObject deleteuser(BankUsers user) throws BadRequestException {
//
//        JSONObject json = new JSONObject();
//
//        userService.deleteUser(user.getEmail());
//
//       json.put("success","BankUsers Deleted Successfully");
//       return json;
//    }


}
