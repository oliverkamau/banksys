package com.link.banksys.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="banksys_user")
public class BankUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name="user_username")
    private String username;

    @Column(name="user_email")
    private String email;

    @Column(name="user_name")
    private String name;

    @Column(name="user_idNo")
    private String idNo;

    @Column(name="user_krapin")
    private String pinNo;

    @Column(name="user_status")
    private String enabled;


    @Column(name="user_password")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "user_reset_pwd")
    private String resetPass;

    @Lob
    @JsonIgnore
    @Column(name="user_photo")
    private byte[] photo;

    @Transient
    MultipartFile file;

    @Column(name="user_dob")
    private Date birthDate;

    @Transient
    private String dob;

    @Column(name="user_gender")
    private String gender;

    @Column(name="user_postaladdress")
    private String postalAddress;

    @Column(name="user_physicaladdress")
    private String physicalAddress;

    @Column(name="user_phone")
    private String phoneNo;

    @Transient
    private String url;




}
