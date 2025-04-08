package com.example.itqan.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
