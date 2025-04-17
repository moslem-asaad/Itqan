package com.example.itqan.repository;

import com.example.itqan.model.Lesson;
import com.example.itqan.model.LessonResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonResourceRepository extends JpaRepository<LessonResource, Integer> {
}
