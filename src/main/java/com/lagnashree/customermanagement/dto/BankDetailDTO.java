package com.lagnashree.customermanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class BankDetailDTO {
    private String personId;
    private String name;
    private Bank bank;
    private List<Card> card;


    @Data
    @Builder
    public static class Bank {
        private String name;
        private String accountNumber;
        private String branchCode;
    }
    @Data
    @Builder
    public static class Card {
        private String cardType;
        private String cardNumber;
        private String paymentNetwork;
    }
}
