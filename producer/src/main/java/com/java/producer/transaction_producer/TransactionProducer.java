package com.java.producer.transaction_producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.producer.domain.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
@Slf4j
public class TransactionProducer {
    private String topic;
    @Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;
    private ObjectMapper objectMapper;
    public CompletableFuture<SendResult<Long, String>> sendTransaction(TransactionRecord transactionRecord) throws JsonProcessingException {
        Long key = transactionRecord.cc_num();
        String value = objectMapper.writeValueAsString(transactionRecord);
        var completableFuture = kafkaTemplate.send(topic, key, value);
        return completableFuture
                .whenComplete((sendResult, throwable) -> {
                    if(throwable != null){
                        handleFailure(key, value, throwable);
                    }else{
                        handleSuccess(key, value, sendResult);
                    }
                });
    }
    private void handleSuccess(Long key, String value, SendResult<Long, String> sendResult) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Long key, String value, Throwable throwable) {
        log.error("Error sending message and the exception is {}", throwable.getMessage());
    }
}
