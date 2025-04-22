package com.example.itqan.repository;

import com.example.itqan.model.Course;
import com.example.itqan.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourseId(int courseId);
}
