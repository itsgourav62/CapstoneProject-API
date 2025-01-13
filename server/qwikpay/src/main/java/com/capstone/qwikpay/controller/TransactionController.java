package com.capstone.qwikpay.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.qwikpay.entities.Transaction;
import com.capstone.qwikpay.services.TransactionService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	@GetMapping("/{transactionId}")
	public ResponseEntity<Optional> getTransactionById(@PathVariable Long transactionId)
	{
		Optional transaction = transactionService.getTransactionById(transactionId);
		if(transaction==null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(transaction);
	}
	@GetMapping("/transactions/{paymentId}")
		public ResponseEntity<Transaction> getTransactionBypaymentId(@PathVariable Long paymentId)
		{
			Transaction transaction =transactionService.getTransactionBypaymentId(paymentId);
			if(transaction==null)
			{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(transaction);
		}
	@PostMapping("/createTransaction")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
	}
