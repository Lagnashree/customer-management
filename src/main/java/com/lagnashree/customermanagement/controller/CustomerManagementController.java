package com.lagnashree.customermanagement.controller;
import com.lagnashree.customermanagement.dto.CustomerDTO;
import com.lagnashree.customermanagement.dto.PatchBankDetailDTO;
import com.lagnashree.customermanagement.exception.InternalException;
import com.lagnashree.customermanagement.exception.InvalidInputException;
import com.lagnashree.customermanagement.service.CustomerManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class CustomerManagementController {
    private final CustomerManagementService customerManagementService;
    @GetMapping("/getDetails/person/{personId}/request/{requestType}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable @NotEmpty String personId, @PathVariable @NotEmpty String requestType) throws InvalidInputException, InternalException {
        try {
            log.info(String.format("Request received for %s with personId=%s", requestType, personId));

            switch (requestType) {
                case "personalDetails":
                    return new ResponseEntity<>(customerManagementService.getCustomerPersonalDetails(personId), HttpStatus.OK);
                case "bankDetails":
                    return new ResponseEntity<>(customerManagementService.getCustomerBankDetails(personId), HttpStatus.OK);
                case "qualifications":
                    return new ResponseEntity<>(customerManagementService.getCustomerQualificationsDetails(personId), HttpStatus.OK);
                default:
                    log.error(String.format("requestType is not valid: %s", requestType));
                    throw new InvalidInputException("invalid requestType value");
            }
        } catch (Exception e) {
            log.error("in exception in controller: " + e);
            throw e;
        }
    }

    @PatchMapping("/updateDetails/bank")
    public ResponseEntity<CustomerDTO> patchBankDetails(@Valid @RequestBody PatchBankDetailDTO patchBankDetailsDTO) throws InternalException, InvalidInputException {
        try {
            return new ResponseEntity<>(customerManagementService.patchBankDetails(patchBankDetailsDTO),
                    HttpStatus.OK);
        } catch (InvalidInputException| InternalException e) {
            throw e;
        }
    }
}
