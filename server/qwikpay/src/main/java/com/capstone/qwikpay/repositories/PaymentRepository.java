package com.capstone.qwikpay.repositories;

import com.capstone.qwikpay.entities.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    //List<Payment> findByBillId(Integer billId);

    List<Payment> findByPaymentStatus(String paymentStatus);
}
