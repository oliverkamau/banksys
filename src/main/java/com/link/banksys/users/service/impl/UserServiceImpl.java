package com.link.banksys.users.service.impl;

import com.link.banksys.exceptions.BadRequestException;
import com.link.banksys.users.model.BankUsers;
import com.link.banksys.users.repo.UserRepository;
import com.link.banksys.users.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private JavaMailSender javaMailSender;
    private BCryptPasswordEncoder encoder;



    public UserServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.encoder = encoder;
    }

    @Override
    public BankUsers getUser(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public void updateProfile(BankUsers bankUsers) {


        BankUsers u = userRepository.findByUserId(bankUsers.getUserId());
        if(bankUsers.getDob()!=null || !bankUsers.getDob().isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date convertedCurrentDate = sdf.parse(bankUsers.getDob());
                u.setBirthDate(convertedCurrentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(bankUsers.getPhoto()!=null){
            u.setPhoto(bankUsers.getPhoto());
        }
        u.setName(bankUsers.getName());
        u.setIdNo(bankUsers.getIdNo());
        u.setPinNo(bankUsers.getPinNo());
        u.setEmail(bankUsers.getEmail());
        u.setGender(bankUsers.getGender());
        u.setPhoneNo(bankUsers.getPhoneNo());
        u.setPhysicalAddress(bankUsers.getPhysicalAddress());
        u.setPostalAddress(bankUsers.getPostalAddress());
        userRepository.save(u);

    }

    @Override
    public Long countByUsername(String username) {
        return userRepository.countByUsername(username);
    }

    @Override
    public BankUsers findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(BankUsers bankUsers, String baseUrl) throws BadRequestException {

        BankUsers u = userRepository.findByUsernameAndEnabled(bankUsers.getUsername(),"1");
        BankUsers e = userRepository.findByEmailAndEnabled(bankUsers.getEmail(),"1");
        if(u!=null){
           throw new BadRequestException("Username already taken find another username");
        }
        if(e!=null){
            throw new BadRequestException("Email already taken find another email");
        }
        bankUsers.setEnabled("0");
        bankUsers.setPassword(encoder.encode(bankUsers.getPassword()));
        BankUsers savedBankUsers = userRepository.save(bankUsers);
        try {
            sendMail(savedBankUsers,baseUrl);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }

    private void sendMail(BankUsers bankUsers, String baseUrl) throws MessagingException, UnsupportedEncodingException {


        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("kamaugaby13@gmail.com","BANKSYS");
        helper.setTo(bankUsers.getEmail());

        helper.setSubject("ACTIVATE YOUR BANKSYS ACCOUNT");
        String token=baseUrl+"/activate?userId="+ bankUsers.getUserId();
        message.setContent("<h2>Activate Your BankSys Account</h2>" +
                "<h3 style=\"color:green\">Please Click on the button below to Activate Account\n </h3> "+
                " <a target='_blank' href="+token+"><button style=\"color:green;\">Activate</button></a>","text/html; charset=utf-8");
        javaMailSender.send(message);


    }

    @Override
    public void activateUser(Long userId) {
        BankUsers u = userRepository.findByUserId(userId);
        u.setEnabled("1");
        userRepository.save(u);
    }

//    @Override
//    @Modifying
//    public void deleteUser(String email) {
//        BankUsers e = userRepository.findByEmail(email);
//        if(e!=null){
//            userRepository.deleteById(e.getUserId());
//        }
//
//    }
}
