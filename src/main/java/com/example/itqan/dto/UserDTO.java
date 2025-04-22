package com.example.itqan.dto;

import com.example.itqan.model.Gender;
import com.example.itqan.model.Role;
import com.example.itqan.model.User;

public class UserDTO {
    public String token;
    public Role role;

    public String name;

    public String userName;

    public int id;

    public Gender gender;

    public UserDTO(String token, Role role, String name, String userName, int id, Gender gender) {
        this.token = token;
        this.role = role;
        this.name = name;
        this.userName = userName;
        this.id = id;
        this.gender = gender;
    }
}
