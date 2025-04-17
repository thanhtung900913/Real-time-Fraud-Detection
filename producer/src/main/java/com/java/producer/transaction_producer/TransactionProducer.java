package com.java.producer.transaction_producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.producer.domain.TransactionRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class TransactionProducer {
    private final Logger log = LoggerFactory.getLogger(TransactionProducer.class);

    private final String topic = "balance_check_command";
    private KafkaTemplate<Long, String> kafkaTemplate;
    private ObjectMapper objectMapper;
    public TransactionProducer(KafkaTemplate<Long, String> kafkaTemplate, ObjectMapper objectMapper){
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
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
