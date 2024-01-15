package com.lagnashree.customermanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PatchBankDetailDTO {
    @NotEmpty
    private String personId;
    @Valid
    @NotNull
    private NewBankDetails newBankDetails;
    @Valid
    @NotNull
    private List<CreditCard> creditCards;

    @Data
    @Builder
    public static class NewBankDetails {
        @NotEmpty
        private String name;
        @NotEmpty
        private String account;
        @NotEmpty
        private String branch;
        @Valid
        @NotNull
        private DebitCard debitCard;

        @Data
        @Builder
        public static class DebitCard {
            @NotEmpty
            private String number;
            @NotEmpty
            private String paymentNetwork;
        }
    }
    @Data
    @Builder
    public static class CreditCard {
        @NotEmpty
        private String number;
        @NotEmpty
        private String paymentNetwork;
    }
}

