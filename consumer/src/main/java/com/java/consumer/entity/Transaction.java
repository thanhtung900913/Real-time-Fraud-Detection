package com.java.consumer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("cc_num")
    private Long ccNum;

    @JsonProperty("merchant")
    private String merchant;

    @JsonProperty("category")
    private String category;

    @JsonProperty("amt")
    private Double amt;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("street")
    private String street;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip")
    private Integer zip;

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("longitude")
    @Column(name = "`long`")
    private Double longitude;

    @JsonProperty("city_pop")
    private Integer cityPop;

    @JsonProperty("job")
    private String job;

    @JsonProperty("merch_lat")
    private Double merchLat;

    @JsonProperty("merch_long")
    private Double merchLong;

    @JsonProperty("merch_zipcode")
    private Double merchZipcode;
}
