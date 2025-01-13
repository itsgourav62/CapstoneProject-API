package com.capstone.qwikpay.services;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Transaction;

public interface TransactionService {

	public Optional getTransactionById(Long transactionId);

	public Transaction createTransaction(Transaction transaction);

	Transaction getTransactionBypaymentId(Long paymentId);



}
