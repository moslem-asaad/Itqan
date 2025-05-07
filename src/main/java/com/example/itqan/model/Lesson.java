package com.example.itqan.model;

import com.example.itqan.exceptions.InvalidModelStateException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-lesson")
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LessonResource> resources = new ArrayList<>();


    //lesson files
    //participant students
    //summary - ai in the future


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<LessonResource> getResources() {
        return resources;
    }

    public void setResources(List<LessonResource> resources) {
        this.resources = resources;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerTeacherId() {
        if (course == null){
            throw new InvalidModelStateException("course is null");
        }
        return this.course.getOwnerTeacherId();
    }

    public List<LessonResource> addResource(LessonResource lessonResource){
        resources.add(lessonResource);
        return resources;
    }

    public void validate() {
        if (title == null || title.trim().isEmpty() || !Character.isLetter(title.trim().charAt(0))) {
            throw new InvalidModelStateException("Title must start with a letter");
        }
    }
}
