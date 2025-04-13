package com.java.producer.domain;

public record TransactionRecord(
        Long cc_num,
        String merchant,
        String category,
        Double amt,
        String gender,
        String street,
        String city,
        String state,
        Integer zip,
        Double lat,
        Double _long,
        Integer city_pop,
        String job,
        Double merch_lat,
        Double merch_long,
        Double merch_zipcode
) {
}
