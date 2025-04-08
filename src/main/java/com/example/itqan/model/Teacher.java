package com.example.itqan.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Teacher extends User{

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> courses;


}
