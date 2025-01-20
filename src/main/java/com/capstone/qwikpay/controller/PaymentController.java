package com.capstone.qwikpay.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.exceptions.PaymentFailedException;
import com.capstone.qwikpay.repositories.BillRepository;
import com.capstone.qwikpay.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private BillRepository billRepository;

    // Process a new payment
    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) throws PaymentFailedException {
        logger.info("Processing payment request for billId: {}", payment.getBillId());

        if (payment.getBillId() == null) {
            logger.error("Bill ID must not be null for the payment request");
            throw new PaymentFailedException("Bill ID must not be null");
        }

        // Manually set the bill using the bill_id
        Bill bill = billRepository.findById(payment.getBillId())
                .orElseThrow(() -> {
                    logger.error("Bill not found with ID: {}", payment.getBillId());
                    return new PaymentFailedException("Bill not found with ID: " + payment.getBillId());
                });

        payment.setBill(bill);

        // Process the payment
        Payment processedPayment = paymentService.processPayment(payment);
        logger.info("Payment processed successfully for billId: {}", payment.getBillId());

        return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
    }

    // Retrieve the payment details by the status
    @GetMapping("/retrieveByStatus/{status}")
    public ResponseEntity<?> getPaymentByStatus(@PathVariable("status") String status) {
        logger.info("Retrieving payments with status: {}", status);
        
        List<Payment> payments = paymentService.getPaymentByStatus(status);

        if (payments == null || payments.isEmpty()) {
            logger.warn("No payments found with status: {}", status);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No payments found with status: " + status);
        }

        logger.info("Retrieved {} payments with status: {}", payments.size(), status);
        return ResponseEntity.ok(payments);
    }

    // Get a payment by ID
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("retrieveById/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") int paymentId) {
        logger.info("Retrieving payment with ID: {}", paymentId);
        
        Payment payment = paymentService.getPaymentById(paymentId);

        if (payment != null) {
            logger.info("Retrieved payment: {}", payment);
        } else {
            logger.warn("No payment found with ID: {}", paymentId);
        }

        return ResponseEntity.ok(payment);
    }

    // Get all payments
    @GetMapping("/retrieveAll")
    public ResponseEntity<List<Payment>> getAllPayments() {
        logger.info("Retrieving all payments");
        
        List<Payment> payments = paymentService.getAllPayments();
        logger.info("Retrieved {} payments", payments.size());

        return ResponseEntity.ok(payments);
    }

    // Update a payment by ID
    @PutMapping("update/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("id") int paymentId, @RequestBody Payment updatedPayment) throws PaymentFailedException {
        logger.info("Updating payment with ID: {} with new details: {}", paymentId, updatedPayment);
        
        Payment payment = paymentService.updatePayment(paymentId, updatedPayment);
        logger.info("Updated payment: {}", payment);

        return ResponseEntity.ok(payment);
    }

    // Delete a payment by ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable("id") int paymentId) {
        logger.info("Deleting payment with ID: {}", paymentId);
        
        paymentService.deletePayment(paymentId);
        logger.info("Payment with ID: {} deleted successfully", paymentId);

        return ResponseEntity.ok("Payment deleted successfully.");
    }
}
