package com.example.itqan.dto;

import com.example.itqan.model.Course;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CourseTimeDTO {

    private Long id;

    private int courseId;

    private DayOfWeek dayOfWeek;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private LocalDateTime nextLessonDate;

    public CourseTimeDTO(Long id, int courseId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, LocalDateTime nextLessonDate) {
        this.id = id;
        this.courseId = courseId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nextLessonDate = nextLessonDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getNextLessonDate() {
        return nextLessonDate;
    }

    public void setNextLessonDate(LocalDateTime nextLessonDate) {
        this.nextLessonDate = nextLessonDate;
    }

    public void isVaild() {
        if(!startTime.isBefore(endTime))
            throw new IllegalArgumentException("start time should be before end time") ;
    }

}
