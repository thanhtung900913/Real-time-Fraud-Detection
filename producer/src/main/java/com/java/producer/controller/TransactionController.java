package com.java.producer.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.producer.domain.TransactionRecord;
import com.java.producer.transaction_producer.TransactionProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TransactionController {
    private final Logger log = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private TransactionProducer transactionProducer;
    @PostMapping("/sendtransaction")
    public ResponseEntity<TransactionRecord> sendTransaction(@RequestBody TransactionRecord transactionRecord) throws JsonProcessingException {
        log.info("transaction: {}",transactionRecord);
        transactionProducer.sendTransaction(transactionRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionRecord);
    }
}
