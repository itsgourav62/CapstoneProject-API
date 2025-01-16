package com.capstone.qwikpay.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.repositories.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment processPayment(Payment payment) {
        payment.setPaymentStatus("PROCESSED"); 
        payment.setPaymentDate(java.time.LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    @Override
    public boolean validatePayment(Integer paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        return payment.isPresent() && "COMPLETED".equalsIgnoreCase(payment.get().getPaymentStatus());
    }

    @Override
    public Payment getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found for ID: " + paymentId));
    }

    @Override
    public List<Payment> getAllPayments() {
        return (List<Payment>) paymentRepository.findAll();
    }

    @Override
    public Payment updatePayment(Integer paymentId, Payment updatedPayment) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found for ID: " + paymentId));
        
        //existingPayment.setBillId(updatedPayment.getBillId());
        existingPayment.setPaymentStatus(updatedPayment.getPaymentStatus());
        existingPayment.setPaymentDate(updatedPayment.getPaymentDate());
        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Integer paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new RuntimeException("Payment not found for ID: " + paymentId);
        }
        paymentRepository.deleteById(paymentId);
    }
}
