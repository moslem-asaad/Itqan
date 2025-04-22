package com.example.itqan.dto;

import com.example.itqan.model.Teacher;
import jakarta.persistence.*;

import java.util.List;

public class CourseResponseDTO {

    public int id;
    public String name;
    public String schedule;
    public int teacherId;
    public int numOfStudents;
}
