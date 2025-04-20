package com.java.consumer.transaction_consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.consumer.entity.Transaction;
import com.java.consumer.jpa.TransactionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {
    private final ObjectMapper objectMapper;
    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "balance_check_command")
    public void addTransaction(ConsumerRecord<Long, String> record){
        try{
            Transaction transaction = objectMapper.readValue(record.value(), Transaction.class);
            System.out.println(record.value());
            transactionRepository.save(transaction);
        }catch (Exception e){
            System.err.println("error :"+e);
        }
    }
}
