package com.capstone.qwikpay.services;

import com.capstone.qwikpay.entities.Bill;

import java.util.List;

public interface BillService {

    // Create new bill
    Bill createBill(Bill bill);

    // Retrieve all bills
    List<Bill> getAllBills();

    // Retrieve bills by User ID
    List<Bill> getBillsByUserId(Integer userId);

    // Retrieve bill by Bill ID
    Bill getBillById(Integer billId);

    // Retrieve bills by Status
    List<Bill> getBillsByStatus(String status);

    // Update bill by its ID
    Bill updateBillById(Integer billId, Bill updatedBill);

    // Delete bill by its ID
    void deleteBillById(Integer billId);
}
