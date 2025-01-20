package com.capstone.qwikpay.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;

@Repository
public interface BillRepository extends CrudRepository<Bill, Integer> {

    List<Bill> findByUserId(Integer userId);
    List<Bill> findByBillStatus(String status);
    
    @Query("SELECT p FROM Payment p JOIN p.bill b WHERE b.user.id = :userId")
    List<Payment> findPaymentsByUserId(Integer userId);
}
