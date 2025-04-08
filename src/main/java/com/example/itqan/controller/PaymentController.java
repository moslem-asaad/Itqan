package com.example.itqan.controller;

import com.example.itqan.model.Payment;
import com.example.itqan.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/student/{studentId}")
    public List<Payment> getPaymentsByStudent(@PathVariable int studentId) {
        return paymentService.getPaymentsByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<Payment> getPaymentsByCourse(@PathVariable int courseId) {
        return paymentService.getPaymentsByCourseId(courseId);
    }

    @PostMapping
    public Payment addPayment(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable int id) {
        paymentService.deletePayment(id);
    }
}
