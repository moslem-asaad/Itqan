package com.example.itqan.service;

import com.example.itqan.model.Course;
import com.example.itqan.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByTeacher(int teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }
}
