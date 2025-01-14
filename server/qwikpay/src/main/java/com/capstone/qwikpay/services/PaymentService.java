package com.capstone.qwikpay.services;

import com.capstone.qwikpay.entities.Payment;

import java.util.List;

public interface PaymentService {

    // Process a new payment
    Payment processPayment(Payment payment);

    // Validate a payment by ID
    boolean validatePayment(Integer paymentId);

    // Retrieve a payment by ID
    Payment getPaymentById(Integer paymentId);

    // Retrieve all payments
    List<Payment> getAllPayments();

    // Update an existing payment
    Payment updatePayment(Integer paymentId, Payment updatedPayment);

    // Delete a payment by ID
    void deletePayment(Integer paymentId);
}
