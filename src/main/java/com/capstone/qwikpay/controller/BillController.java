package com.capstone.qwikpay.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.capstone.qwikpay.exceptions.BillNotFoundException;
import com.capstone.qwikpay.services.BillService;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin("*")
public class BillController {

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    private BillService billService;

    // Create new bill
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        logger.info("Request to create a new bill with details: {}", bill);
        Bill createdBill = billService.createBill(bill);
        logger.info("Bill created successfully with ID: {}", createdBill.getBillId());
        return ResponseEntity.ok(createdBill);
    }

    // Get all bills
    @GetMapping("/retrievAll")
    public ResponseEntity<List<Bill>> getAllBills() {
        logger.info("Request to retrieve all bills");
        List<Bill> bills = billService.getAllBills();
        logger.info("Retrieved {} bills", bills.size());
        return ResponseEntity.ok(bills);
    }

    // Get bills by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable Integer userId) {
        logger.info("Request to retrieve bills for userId: {}", userId);
        List<Bill> bills = billService.getBillsByUserId(userId);
        logger.info("Retrieved {} bills for userId: {}", bills.size(), userId);
        return ResponseEntity.ok(bills);
    }

    // Get bill by billId
    @GetMapping("retrieveBillById/{billId}")
    public ResponseEntity<Bill> getBillByBillId(@PathVariable Integer billId) throws BillNotFoundException {
        logger.info("Request to retrieve bill with billId: {}", billId);
        Bill bill = billService.getBillById(billId);
        logger.info("Bill retrieved with ID: {}", bill.getBillId());
        return ResponseEntity.ok(bill);
    }

    // Get bills by status
    @GetMapping("/{status}")
    public ResponseEntity<List<Bill>> getBillsByStatus(@PathVariable String status) {
        logger.info("Request to retrieve bills with status: {}", status);
        List<Bill> bills = billService.getBillsByStatus(status);
        logger.info("Retrieved {} bills with status: {}", bills.size(), status);
        return ResponseEntity.ok(bills);
    }

    // Update bill by billId
    @PutMapping("update/{billId}")
    public ResponseEntity<Bill> updateBillById(@PathVariable Integer billId, @RequestBody Bill updatedBill) throws BillNotFoundException {
        logger.info("Request to update bill with billId: {} with new details: {}", billId, updatedBill);
        Bill bill = billService.updateBillById(billId, updatedBill);
        logger.info("Bill updated successfully with ID: {}", bill.getBillId());
        return ResponseEntity.ok(bill);
    }

    // Delete bill by billId
    @DeleteMapping("delete/{billId}")
    public ResponseEntity<Void> deleteBillById(@PathVariable Integer billId) throws BillNotFoundException {
        logger.info("Request to delete bill with billId: {}", billId);
        billService.deleteBillById(billId);
        logger.info("Bill with billId: {} deleted successfully", billId);
        return ResponseEntity.noContent().build();
    }
}
