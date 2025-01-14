package com.capstone.qwikpay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Process a new payment
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody Payment payment) {
        paymentService.processPayment(payment);
        return ResponseEntity.ok("Payment processed successfully.");
    }

    // Validate a payment by ID it is used to show that bill payment is completed or not
    @GetMapping("/status/{id}")
    public ResponseEntity<Boolean> validatePayment(@PathVariable("id") int paymentId) {
        boolean isValid = paymentService.validatePayment(paymentId);
        return ResponseEntity.ok(isValid);
    }

    // Get a payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") int paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    // Get all payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Update a payment by ID
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("id") int paymentId, @RequestBody Payment updatedPayment) {
        Payment payment = paymentService.updatePayment(paymentId, updatedPayment);
        return ResponseEntity.ok(payment);
    }

    // Delete a payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable("id") int paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok("Payment deleted successfully.");
    }
}
