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
    ) {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseRequestDTO dto) {
        Course savedCourse = courseService.saveCourse(dto);
        return ResponseEntity.ok(savedCourse);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
    }

    @PutMapping("/teacher/{teacherId}/{courseId}")
    public ResponseEntity<?> editCourse(
            @PathVariable int teacherId,
            @PathVariable int courseId,
            @RequestBody CourseRequestDTO dto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.TEACHER && user.getId() != teacherId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
        if (user.getRole() == Role.TEACHER && dto.getTeacherId() != user.getId()) {
            throw new RuntimeException("You can only edit your own courses.");
        }


        try {
            Course updatedCourse = courseService.updateCourse(courseId, dto);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
