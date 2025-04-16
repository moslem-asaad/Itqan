package com.example.itqan.dto;

import com.example.itqan.model.Gender;
import com.example.itqan.model.Role;
import com.example.itqan.model.User;

public class RegisterRequest {

    public String userName;
    public String name;
    public String email;
    public String password;
    public String phoneNumber;
    public Role role;

    public Gender gender;
}

