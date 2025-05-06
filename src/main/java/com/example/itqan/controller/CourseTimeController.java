package com.example.itqan.controller;

import com.example.itqan.model.CourseTime;
import com.example.itqan.service.CourseTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-times")
public class CourseTimeController {

    @Autowired
    private CourseTimeService courseTimeService;

    @GetMapping("/next")
    public ResponseEntity<List<CourseTime>> getNextLessonsForTeacher(
            @RequestParam Long teacherId,
            @RequestParam(defaultValue = "0") int page) {
        int size = 4;
        try {
            List<CourseTime> lessons = courseTimeService.getNextLessonsForTeacher(teacherId, page, size);
            if (lessons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
