package com.capstone.qwikpay.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.Transaction;
import com.capstone.qwikpay.repositories.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
    private TransactionRepository transactionRepository;

	@Override
    public Optional getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        // Save the transaction to the database using the CrudRepository
        return transactionRepository.save(transaction);
    }

	@Override
	public Transaction getTransactionBypaymentId(Long paymentId) {
		// TODO Auto-generated method stub
		return transactionRepository.findByPaymentId(paymentId);
	}

}
