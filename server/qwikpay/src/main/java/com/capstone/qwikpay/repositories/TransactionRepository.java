package com.capstone.qwikpay.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Transaction;
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {


	Transaction findByPaymentId(Long paymentId);

	Transaction save(Transaction transaction);

	Optional findById(Long transactionId);


}
