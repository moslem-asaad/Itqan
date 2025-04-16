package com.example.itqan.model;

import jakarta.persistence.*;

@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    //lesson files
    //participant students
    //summary - ai in the future

}
