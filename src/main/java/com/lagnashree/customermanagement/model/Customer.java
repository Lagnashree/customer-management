package com.lagnashree.customermanagement.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
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
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bank {
        private String bankName;
        private String accountNumber;
        private String branch;
        private DebitCard debitCard;
        private CreditCard creditCard;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DebitCard {
        private String number;
        private String paymentNetwork;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreditCard {
        private String number;
        private String paymentNetwork;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Education {
        private School school;
        private Collage collage;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class School {
        private String name;
        private String address;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Collage {
        private String name;
        private String address;
        private String degree;
    }
}
