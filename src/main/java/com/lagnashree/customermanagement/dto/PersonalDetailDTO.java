package com.lagnashree.customermanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonalDetailDTO {
    private String name;
    private String personId;
    private String dob;
    private Address address;
    private List<Contact> contact;

    @Data
    @Builder
    public static class Address {
        private String street;
        private String houseNumber;
        private String city;
        private String country;
        private String countryCode;
    }

    @Data
    @Builder
    public static class Contact {
        private String code;
        private String number;
    }
}
