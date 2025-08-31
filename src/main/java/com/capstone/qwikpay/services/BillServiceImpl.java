package com.capstone.qwikpay.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.exceptions.BillNotFoundException;
import com.capstone.qwikpay.repositories.BillRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private BillRepository billRepository;

    // Create new bill
    @Override
    @Transactional
    public Bill createBill(Bill bill) {
        logger.info("Creating new bill with details: {}", bill);
        bill.setCreatedAt(LocalDateTime.now());
        Bill createdBill = billRepository.save(bill);
        logger.info("Created new bill with ID: {}", createdBill.getBillId());
        return createdBill;
    }

    // Retrieve all bills
    @Override
    public List<Bill> getAllBills() {
        logger.info("Fetching all bills from repository.");
        List<Bill> bills = (List<Bill>) billRepository.findAll();
        logger.info("Retrieved {} bills.", bills.size());
        return bills;
    }

    // Retrieve bills by User ID
    @Override
    public List<Bill> getBillsByUserId(Integer userId) {
        logger.info("Fetching bills for user ID: {}", userId);
        List<Bill> bills = billRepository.findByUserId(userId);
        logger.info("Retrieved {} bills for user ID: {}", bills.size(), userId);
        return bills;
    }

    // Retrieve bill by Bill ID
    @Override
    public Bill getBillById(Integer billId) throws BillNotFoundException {
        logger.info("Fetching bill with ID: {}", billId);
        Optional<Bill> bill = billRepository.findById(billId);
        if (bill.isPresent()) {
            logger.info("Found bill with ID: {}", billId);
            return bill.get();
        } else {
            logger.error("Bill not found with ID: {}", billId);
            throw new BillNotFoundException("Bill not found with id: " + billId);
        }
    }

    // Retrieve bills by Status
    @Override
    public List<Bill> getBillsByStatus(String status) {
        logger.info("Fetching bills with status: {}", status);
        List<Bill> bills = billRepository.findByBillStatus(status);
        logger.info("Retrieved {} bills with status: {}", bills.size(), status);
        return bills;
    }

    // Update bill by its ID
    @Override
    public Bill updateBillById(Integer billId, Bill updatedBill) throws BillNotFoundException {
        logger.info("Updating bill with ID: {}", billId);
        Bill existingBill = getBillById(billId);  // Fetch the existing bill
        existingBill.setAmount(updatedBill.getAmount());
        existingBill.setBillStatus(updatedBill.getBillStatus());
        existingBill.setDescription(updatedBill.getDescription());
        existingBill.setDueDate(updatedBill.getDueDate());
        existingBill.setUpdatedAt(LocalDateTime.now());
        
        Bill updated = billRepository.save(existingBill);
        logger.info("Updated bill with ID: {}", billId);
        return updated;
    }

    // Delete bill by its ID
    @Override
    public void deleteBillById(Integer billId) throws BillNotFoundException {
        logger.info("Deleting bill with ID: {}", billId);
        Bill bill = getBillById(billId);  // Fetch the bill
        billRepository.delete(bill);
        logger.info("Deleted bill with ID: {}", billId);
    }
}
