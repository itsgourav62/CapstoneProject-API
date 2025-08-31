package com.capstone.qwikpay.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.exceptions.PaymentFailedException;
import com.capstone.qwikpay.repositories.BillRepository;
import com.capstone.qwikpay.repositories.PaymentRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    @Transactional
    @Override
    public Payment processPayment(Payment payment) throws PaymentFailedException {
        logger.info("Processing payment for Bill ID: {}", payment.getBillId());

        // Ensure bill_id is provided
        if (payment.getBill() == null || payment.getBill().getBillId() == null) {
            logger.error("Bill information is missing or Bill ID is null for the payment.");
            throw new PaymentFailedException("Bill information is missing or Bill ID is null for the payment.");
        }

        // Fetch the Bill using the provided Bill ID
        Bill bill = billRepository.findById(payment.getBill().getBillId())
                .orElseThrow(() -> {
                    logger.error("Bill not found with ID: {}", payment.getBill().getBillId());
                    return new PaymentFailedException("Bill not found with ID: " + payment.getBill().getBillId());
                });

        // Associate the Bill with the Payment
        payment.setBill(bill);

        // Update payment status
        payment.setPaymentStatus("Paid");
        payment.setPaymentDate(LocalDateTime.now());

        // Update the bill status based on payment status
        if ("Paid".equals(payment.getPaymentStatus())) {
            bill.setBillStatus("Paid");
        } else {
            bill.setBillStatus("Pending");
        }

        // Save the updated bill and payment
        Payment savedPayment = paymentRepository.save(payment);
        bill.setPayment(savedPayment);
        billRepository.save(bill);
        
        logger.info("Payment processed successfully with Payment ID: {}", savedPayment.getPmtId());
        return savedPayment;
    }

    @Override
    public boolean validatePayment(Integer paymentId) throws PaymentFailedException {
        logger.info("Validating payment with Payment ID: {}", paymentId);
        return paymentRepository.findById(paymentId)
                .map(payment -> {
                    boolean isValid = "Paid".equalsIgnoreCase(payment.getPaymentStatus());
                    logger.info("Payment validation result for Payment ID {}: {}", paymentId, isValid);
                    return isValid;
                })
                .orElseThrow(() -> {
                    logger.error("Payment not found for ID: {}", paymentId);
                    return new PaymentFailedException("Payment not found for ID: " + paymentId);
                });
    }

    @Override
    public Payment getPaymentById(Integer paymentId) {
        logger.info("Fetching payment with Payment ID: {}", paymentId);
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    logger.error("Payment not found for ID: {}", paymentId);
                    return new RuntimeException("Payment not found for ID: " + paymentId);
                });
    }

    @Override
    public List<Payment> getAllPayments() {
        logger.info("Fetching all payments");
        List<Payment> payments = (List<Payment>) paymentRepository.findAll();
        logger.info("Total number of payments retrieved: {}", payments.size());
        return payments;
    }

    @Override
    @Transactional
    public Payment updatePayment(Integer paymentId, Payment updatedPayment) throws PaymentFailedException {
        logger.info("Updating payment with Payment ID: {} and new details: {}", paymentId, updatedPayment);

        // Retrieve the existing payment
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    logger.error("Payment not found for ID: {}", paymentId);
                    return new RuntimeException("Payment not found for ID: " + paymentId);
                });

        // Check if the payment is already completed and prevent modification
        if ("Paid".equalsIgnoreCase(existingPayment.getPaymentStatus()) || 
            "COMPLETED".equalsIgnoreCase(existingPayment.getPaymentStatus())) {
            logger.error("Cannot update a completed payment with Payment ID: {}", paymentId);
            throw new PaymentFailedException("Cannot update a completed payment.");
        }

        // Update the payment details
        existingPayment.setAmount(updatedPayment.getAmount());
        existingPayment.setPaymentStatus(updatedPayment.getPaymentStatus());
        existingPayment.setPaymentDate(LocalDateTime.now());

        // Update the associated bill status based on the payment status
        Bill bill = existingPayment.getBill();
        if (bill != null) {
            if ("PAID".equalsIgnoreCase(updatedPayment.getPaymentStatus()) || 
                "Paid".equalsIgnoreCase(updatedPayment.getPaymentStatus())) {
                bill.setBillStatus("PAID");  // Set the bill status to PAID when payment is completed
            } else {
                bill.setBillStatus("Partially Paid");  // Set to partially paid if not fully completed
            }
            bill.setUpdatedAt(LocalDateTime.now()); // Update the updated timestamp
            billRepository.save(bill);  // Save the updated bill
        }

        // Save the updated payment
        Payment savedPayment = paymentRepository.save(existingPayment);
        logger.info("Payment updated successfully with Payment ID: {}", savedPayment.getPmtId());
        return savedPayment;
    }

    @Override
    public void deletePayment(Integer paymentId) {
        logger.info("Deleting payment with Payment ID: {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    logger.error("Payment not found for ID: {}", paymentId);
                    return new RuntimeException("Payment not found for ID: " + paymentId);
                });

        Bill bill = payment.getBill();
        if (bill != null) {
            bill.setPayment(null);
            bill.setBillStatus("Unpaid");
            billRepository.save(bill);
            logger.info("Associated bill for Payment ID: {} updated to 'Unpaid'", paymentId);
        }

        paymentRepository.deleteById(paymentId);
        logger.info("Payment with Payment ID: {} deleted successfully", paymentId);
    }

    @Override
    public List<Payment> getPaymentByStatus(String paymentStatus) {
        if (paymentStatus == null || paymentStatus.trim().isEmpty()) {
            logger.error("Payment status cannot be null or empty");
            throw new IllegalArgumentException("Payment status cannot be null or empty");
        }
        logger.info("Fetching payments with status: {}", paymentStatus);
        List<Payment> payments = paymentRepository.findByPaymentStatus(paymentStatus);
        logger.info("Total payments retrieved with status '{}': {}", paymentStatus, payments.size());
        return payments;
    }

    @Override
    public List<Payment> getPaymentsByUserId(int userId) {
        // Assuming you have a user relationship with payments, fetch payments by userId
        logger.info("Fetched payments with User ID: {}", userId);
        return paymentRepository.findPaymentsByUserId(userId);
    }
}
