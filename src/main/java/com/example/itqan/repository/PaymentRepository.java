package com.example.itqan.repository;

import com.example.itqan.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByStudentId(int studentId);
    List<Payment> findByCourseId(int courseId);
}
