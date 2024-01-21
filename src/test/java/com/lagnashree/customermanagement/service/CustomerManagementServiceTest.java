package com.lagnashree.customermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lagnashree.customermanagement.dto.*;
import com.lagnashree.customermanagement.exception.InvalidInputException;
import com.lagnashree.customermanagement.model.Customer;
import com.lagnashree.customermanagement.repository.CustomerManagementRepository;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CustomerManagementServiceV2Test {
    @InjectMocks
    private CustomerManagementService customerManagementService;
    @Mock
    CustomerManagementRepository customerManagementRepository;
    @Test
    public void CustomerManagementService_GetCustomerPersonalDetails_ValidResponse() throws ParseException, IOException {
        String file ="src/test/resources/Person.json";
        InputStream inputStream = new FileInputStream(file);
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customerObj = objectMapper.readValue(inputStream, Customer.class);
        Mockito.when(customerManagementRepository.readCustomerData()).thenReturn(customerObj);
        PersonalDetailDTO personalDetail = customerManagementService.getCustomerPersonalDetails("12345");
        assertEquals("Peter Nilson", personalDetail.getName());
        assertEquals("12345", personalDetail.getPersonId());
        assertEquals("01/01/1990", personalDetail.getDob());
        assertEquals("Malmö", personalDetail.getAddress().getCity());
        assertEquals("Sweden", personalDetail.getAddress().getCountry());
        assertEquals("SE", personalDetail.getAddress().getCountryCode());
        assertEquals("123", personalDetail.getAddress().getHouseNumber());
        assertEquals(2, personalDetail.getContact().size());
        assertEquals("783946273", personalDetail.getContact().get(0).getNumber());
        assertEquals("+46", personalDetail.getContact().get(0).getCode());
    }
    @Test
    public void CustomerManagementService_getCustomerBankDetails_ValidResponse() throws ParseException, IOException {
        String file ="src/test/resources/Person.json";
        InputStream inputStream = new FileInputStream(file);
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customerObj = objectMapper.readValue(inputStream, Customer.class);
        Mockito.when(customerManagementRepository.readCustomerData()).thenReturn( customerObj);
        BankDetailDTO bankDetail = customerManagementService.getCustomerBankDetails("12345");
        assertEquals("Peter Nilson", bankDetail.getName());
        assertEquals("12345", bankDetail.getPersonId());
        assertEquals("xyz", bankDetail.getBank().getName());
        assertEquals("1234567890", bankDetail.getBank().getAccountNumber());
        assertEquals(2, bankDetail.getCard().size());
        assertEquals("1234 8273 9384", bankDetail.getCard().get(0).getCardNumber());
        assertEquals("Credit", bankDetail.getCard().get(0).getCardType());
        assertEquals("Mastero", bankDetail.getCard().get(0).getPaymentNetwork());
    }
    @Test
    public void CustomerManagementService_patchBankDetails_ValidResponse() throws ParseException, IOException {
        String file ="src/test/resources/Person.json";
        InputStream inputStream = new FileInputStream(file);
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customerObj = objectMapper.readValue(inputStream, Customer.class);
        Mockito.when(customerManagementRepository.readCustomerData()).thenReturn(customerObj);
        PatchBankDetailDTO patchBankDetail = PatchBankDetailDTO.builder()
                .personId("12345")
                .newBankDetails(PatchBankDetailDTO.NewBankDetails.builder()
                        .name("XYZ Bank")
                        .account("123456789")
                        .branch("Branch123")
                        .debitCard(PatchBankDetailDTO.NewBankDetails.DebitCard.builder()
                                .number("1234 5678 9101 1122")
                                .paymentNetwork("VISA")
                                .build())
                        .build())
                .creditCards(List.of(
                        PatchBankDetailDTO.CreditCard.builder()
                                .number("9876 5432 1010 1122")
                                .paymentNetwork("MasterCard")
                                .build(),
                        PatchBankDetailDTO.CreditCard.builder()
                                .number("4567 8901 2345 6789")
                                .paymentNetwork("AMEX")
                                .build()
                ))
                .build();
        CustomerDTO customerDTO = customerManagementService.patchBankDetails(patchBankDetail);
        assertEquals("Peter", customerDTO.getFirstName());
        assertEquals("SE", customerDTO.getCountryCode());
        assertEquals(4, customerDTO.getPhone().size());
        assertEquals("XYZ Bank", customerDTO.getBank().getBankName());
        assertEquals("123456789", customerDTO.getBank().getAccountNumber());
        assertEquals("Branch123", customerDTO.getBank().getBranch());
        assertEquals("1234 5678 9101 1122", customerDTO.getBank().getDebitCard().getNumber());
        assertEquals("VISA", customerDTO.getBank().getDebitCard().getPaymentNetwork());
        assertEquals(2, customerDTO.getBank().getCreditCard().size());
        assertEquals("9876 5432 1010 1122", customerDTO.getBank().getCreditCard().get(0).getNumber());
        assertEquals("MasterCard", customerDTO.getBank().getCreditCard().get(0).getPaymentNetwork());
        assertEquals("xyz school", customerDTO.getEducation().getSchool().getName());
        assertEquals("Dalaplan Malmo 21744 Sweden", customerDTO.getEducation().getSchool().getAddress());
        assertEquals("Abc collage", customerDTO.getEducation().getCollege().getName());
        assertEquals("Abc Malmö 72863 Sweden", customerDTO.getEducation().getCollege().getAddress());
        assertEquals("MS", customerDTO.getEducation().getCollege().getDegree());

    }
    @Test
    public void CustomerManagementService_GetCustomerPersonalDetails_InvalidPersonId() throws ParseException, IOException  {

        String file ="src/test/resources/Person.json";
        InputStream inputStream = new FileInputStream(file);
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customerObj = objectMapper.readValue(inputStream, Customer.class);
        Mockito.when(customerManagementRepository.readCustomerData()).thenReturn(customerObj);
        assertThrows(InvalidInputException.class, () ->
                customerManagementService.getCustomerPersonalDetails("123456"));
    }
    @Test
    public void CustomerManagementService_getCustomerBankDetails_InvalidPersonId() throws ParseException, IOException  {

        String file ="src/test/resources/Person.json";
        InputStream inputStream = new FileInputStream(file);
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customerObj = objectMapper.readValue(inputStream, Customer.class);
        Mockito.when(customerManagementRepository.readCustomerData()).thenReturn(customerObj);
        assertThrows(InvalidInputException.class, () ->
                customerManagementService.getCustomerBankDetails("123456"));
    }
    @Test
    public void CustomerManagementService_getCustomerQualificationsDetails_InvalidPersonId() throws ParseException, IOException  {

        String file ="src/test/resources/Person.json";
        InputStream inputStream = new FileInputStream(file);
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customerObj = objectMapper.readValue(inputStream, Customer.class);
        Mockito.when(customerManagementRepository.readCustomerData()).thenReturn(customerObj);
        assertThrows(InvalidInputException.class, () ->
                customerManagementService.getCustomerQualificationsDetails("123456"));
    }
}
