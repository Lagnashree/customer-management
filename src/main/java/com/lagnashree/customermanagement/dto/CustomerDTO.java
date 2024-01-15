package com.lagnashree.customermanagement.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
public class CustomerDTO {
    private String firstName;
    private String lastName;
    private String personId;
    private String dob;
    private String streetName;
    private String houseNumber;
    private String city;
    private String country;
    private String countryCode;
    private List<String> phone;
    private Bank bank;
    private Education education;

    @Data
    @Builder
    public static class Bank {
        private String bankName;
        private String accountNumber;
        private String branch;
        private DebitCard debitCard;
        private List<CreditCard> creditCard;
    }

    @Data
    @Builder
    public static class DebitCard {
        private String number;
        private String paymentNetwork;
    }

    @Data
    @Builder
    public static class CreditCard {
        private String number;
        private String paymentNetwork;
    }

    @Data
    @Builder
    public static class Education {
        private School school;
        private College college;
    }

    @Data
    @Builder
    public static class School {
        private String name;
        private String address;
    }

    @Data
    @Builder
    public static class College {
        private String name;
        private String address;
        private String degree;
    }
}