package com.example.itqan.service;

import com.example.itqan.model.Payment;
import com.example.itqan.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getPaymentsByStudentId(int studentId) {
        return paymentRepository.findByStudentId(studentId);
    }

    public List<Payment> getPaymentsByCourseId(int courseId) {
        return paymentRepository.findByCourseId(courseId);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(int id) {
        paymentRepository.deleteById(id);
    }
}
