package com.java.consumer.jpa;

import com.java.consumer.entity.CheckedTransaction;
import com.java.consumer.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<CheckedTransaction, Long> {
}
