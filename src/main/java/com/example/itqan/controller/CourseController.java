package com.example.itqan.controller;

import com.example.itqan.dto.CourseRequestDTO;
import com.example.itqan.model.Course;
import com.example.itqan.model.Role;
import com.example.itqan.model.User;
import com.example.itqan.service.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://fox-one-promptly.ngrok-free.app"
})
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getCoursesByTeacher(
            @PathVariable int teacherId,
            Authentication authentication
    ) throws IllegalAccessException {

        List<Course> courses = courseService.getCoursesByTeacher(teacherId,authentication);
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseRequestDTO dto,Authentication authentication) throws IllegalAccessException {
        Course savedCourse = courseService.saveCourse(dto,authentication);
        return ResponseEntity.ok(savedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable int id,Authentication authentication) throws IllegalAccessException {
        courseService.deleteCourse(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/teacher/{teacherId}/{courseId}")
    public ResponseEntity<?> editCourse(
            @PathVariable int teacherId,
            @PathVariable int courseId,
            @RequestBody CourseRequestDTO dto,
            Authentication authentication
    ) throws IllegalAccessException {
        Course updatedCourse = courseService.updateCourse(teacherId,courseId, dto,authentication);
        return ResponseEntity.ok(updatedCourse);
    }

}
