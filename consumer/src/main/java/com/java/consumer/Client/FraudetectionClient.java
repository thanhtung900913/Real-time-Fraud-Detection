package com.java.consumer.Client;

import com.java.consumer.entity.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FraudetectionClient {
    private final RestTemplate restTemplate;

    public FraudetectionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public Boolean checkFraud(Transaction transaction){
        String apiURL = "${API_URL}";
        String response = restTemplate.postForObject(apiURL, transaction, String.class);
        return Boolean.parseBoolean(response);
    }
}
