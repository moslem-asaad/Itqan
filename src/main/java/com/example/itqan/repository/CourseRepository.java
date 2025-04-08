package com.example.itqan.repository;

import com.example.itqan.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTeacherId(int teacherId);
}
