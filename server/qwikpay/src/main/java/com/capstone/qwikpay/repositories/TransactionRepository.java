package com.capstone.qwikpay.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}
