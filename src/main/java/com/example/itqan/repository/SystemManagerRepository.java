package com.example.itqan.repository;

import com.example.itqan.model.SystemManager;
import com.example.itqan.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemManagerRepository extends JpaRepository<SystemManager,Integer> {
    Optional<SystemManager> findByEmail(String email);
}
