package com.capstone.qwikpay.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    //List<Payment> findByBillId(Integer billId);

	// Fetch payment by its ID
//	Optional<Payment> findById(Integer pmtId);  
    List<Payment> findByPaymentStatus(String paymentStatus);
    
//    List<Payment> findByUserId(int userId);
 
    // Fetch payments based on userId (through the Bill -> UserEntity relationship)
    @Query("SELECT p FROM Payment p WHERE p.bill.user.id = :userId")
    List<Payment> findPaymentsByUserId(@Param("userId") int userId);
}
