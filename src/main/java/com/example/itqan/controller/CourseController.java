package com.example.itqan.controller;

import com.example.itqan.model.Course;
import com.example.itqan.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/teacher/{teacherId}")
    public List<Course> getCoursesByTeacher(@PathVariable int teacherId) {
        return courseService.getCoursesByTeacher(teacherId);
    }

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
    }
}
