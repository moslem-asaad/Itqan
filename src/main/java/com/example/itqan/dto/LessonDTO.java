package com.example.itqan.dto;

import com.example.itqan.model.Course;
import com.example.itqan.model.LessonResource;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

public class LessonDTO {

    public int id;

    public String title;

    public String description;


    public int courseId;

    public List<LessonResource> resources = new ArrayList<>();

    public LessonDTO(int id, String title, String description, int courseId, List<LessonResource> resources) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.courseId = courseId;
        this.resources = resources;
    }
}
