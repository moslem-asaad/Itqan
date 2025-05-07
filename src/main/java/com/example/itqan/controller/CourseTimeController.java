package com.example.itqan.controller;

import com.example.itqan.dto.CourseTimeDTO;
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
    public ResponseEntity<List<CourseTimeDTO>> getNextLessonsForTeacher(
            @RequestParam Long teacherId,
            @RequestParam(defaultValue = "0") int page) {
        int size = 4;
        try {
            List<CourseTimeDTO> lessons = courseTimeService.getNextLessonsForTeacher(teacherId, page, size);
            if (lessons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
