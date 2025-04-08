package com.example.itqan.repository;

import com.example.itqan.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    // Add custom queries here if needed
    Optional<Teacher> findByEmail(String email);
}
