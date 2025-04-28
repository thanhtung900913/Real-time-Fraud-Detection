package com.java.consumer.transaction_consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.consumer.Client.FraudetectionClient;
import com.java.consumer.entity.CheckedTransaction;
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
    private final FraudetectionClient fraudetectionClient;
    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionConsumer(ObjectMapper objectMapper, FraudetectionClient fraudetectionClient) {
        this.objectMapper = objectMapper;
        this.fraudetectionClient = fraudetectionClient;
    }

    @KafkaListener(topics = "balance_check_command")
    public void addTransaction(ConsumerRecord<Long, String> record){
        try{
            Transaction transaction = objectMapper.readValue(record.value(), Transaction.class);
            System.out.println(record.value());
            Boolean response = fraudetectionClient.checkFraud(transaction);
            CheckedTransaction checkedTransaction = new CheckedTransaction(
                    null,
                    transaction.getCcNum(),
                    transaction.getMerchant(),
                    transaction.getCategory(),
                    transaction.getAmt(),
                    transaction.getGender(),
                    transaction.getStreet(),
                    transaction.getCity(),
                    transaction.getState(),
                    transaction.getZip(),
                    transaction.getLat(),
                    transaction.getLongitude(),
                    transaction.getCityPop(),
                    transaction.getJob(),
                    transaction.getMerchLat(),
                    transaction.getMerchLong(),
                    transaction.getMerchZipcode(),
                    response
            );
            if(response){
                System.out.println("Warning !!!");
            }
            transactionRepository.save(checkedTransaction);
        }catch (Exception e){
            System.err.println("error :"+e);
        }
    }
}
