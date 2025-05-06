package com.example.itqan.dto;

import com.example.itqan.model.CourseTime;

import java.util.List;

public class CourseRequestDTO {
    private String name;
    private List<CourseTime> schedule;
    private int teacherId;
    private List<Integer> studentIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseTime> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<CourseTime> schedule) {
        this.schedule = schedule;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public List<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.studentIds = studentIds;
    }

    // Getters and setters
}

