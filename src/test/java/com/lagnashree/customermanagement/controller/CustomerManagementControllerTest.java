package com.lagnashree.customermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lagnashree.customermanagement.dto.*;
import com.lagnashree.customermanagement.exception.InvalidInputException;
import com.lagnashree.customermanagement.service.CustomerManagementService;
import com.lagnashree.customermanagement.exception.CustomExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CustomerManagementController.class)
public class CustomerManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    CustomerManagementService customerManagementService;
    @InjectMocks
    private CustomerManagementController customerManagementController;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(customerManagementController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }
    @Test
    public void customerManagementController_getCustomerDetails_personalDetails_success() throws Exception{
        PersonalDetailDTO personalDetail= PersonalDetailDTO.builder()
                .name("Peter Nilson")
                .personId("12345")
                .dob("01/01/1990")
                .address(PersonalDetailDTO.Address.builder()
                        .street("Sodertorp")
                        .houseNumber("123")
                        .city("Malmö")
                        .country("Sweden")
                        .countryCode("SE")
                        .build())
                .contact(Arrays.asList(
                        PersonalDetailDTO.Contact.builder().code("+46").number("783946273").build(),
                        PersonalDetailDTO.Contact.builder().code("+49").number("763445673").build()))
                .build();
        Mockito.when(customerManagementService.getCustomerPersonalDetails(Mockito.any())).thenReturn(personalDetail);
        mockMvc.perform(get("/getDetails/person/12345/request/personalDetails")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*",hasSize(5)))
                .andExpect(jsonPath("$.name").value("Peter Nilson"))
                .andExpect(jsonPath("$.personId").value("12345"))
                .andExpect(jsonPath("$.address.*",hasSize(5)))
                .andExpect(jsonPath("$.address.street").value("Sodertorp"))
                .andExpect(jsonPath("$.address.country").value("Sweden"))
                .andExpect(jsonPath("$.address.houseNumber").value("123"))
                .andExpect(jsonPath("$.address.city").value("Malmö"))
                .andExpect(jsonPath("$.contact",hasSize(2)))
                .andExpect(jsonPath("$.contact[0].code").value("+46"))
                .andExpect(jsonPath("$.contact[0].number").value("783946273"))
                .andExpect(jsonPath("$.contact[1].code").value("+49"))
                .andExpect(jsonPath("$.contact[1].number").value("763445673"));


    }
    @Test
    public void customerManagementController_getCustomerDetails_personalDetails_failure_invalidPersonId() throws Exception{
        String personId = "123456";
        String requestType = "personalDetails";
        Mockito.when(customerManagementService.getCustomerPersonalDetails(Mockito.any())).thenThrow(new InvalidInputException("invalid person id"));
        mockMvc.perform(get("/getDetails/person/{personId}/request/{requestType}",personId, requestType)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.*",hasSize(3)))
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andExpect(jsonPath("$.description").value("invalid person id"));
    }

    @Test
    public void customerManagementController_getCustomerDetails_personalDetails_failure_invalidRequestType() throws Exception{
        String personId = "12345";
        String requestType = "invalidType";
        mockMvc.perform(get("/getDetails/person/{personId}/request/{requestType}",personId, requestType)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.*",hasSize(3)))
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andExpect(jsonPath("$.description").value("invalid requestType value"));

    }
    @Test
    public void customerManagementController_getCustomerDetails_bankDetails_success() throws Exception{
        BankDetailDTO bankDetail = BankDetailDTO.builder()
                .personId("12345")
                .name("Peter Nilson")
                .bank(BankDetailDTO.Bank.builder()
                        .name("xyz bank")
                        .accountNumber("1234567890")
                        .branchCode("6712")
                        .build())
                .card(Arrays.asList(
                        BankDetailDTO.Card.builder()
                                .cardType("Debit")
                                .cardNumber("7653 8273 9384")
                                .paymentNetwork("VISA")
                                .build(),
                        BankDetailDTO.Card.builder()
                                .cardType("Credit")
                                .cardNumber("1234 8273 9384")
                                .paymentNetwork("Mastero")
                                .build()
                ))
                .build();
        Mockito.when(customerManagementService.getCustomerBankDetails(Mockito.any())).thenReturn(bankDetail);
        mockMvc.perform(get("/getDetails/person/12345/request/bankDetails")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*",hasSize(4)))
                .andExpect(jsonPath("$.name").value("Peter Nilson"))
                .andExpect(jsonPath("$.personId").value("12345"))
                .andExpect(jsonPath("$.bank.*",hasSize(3)))
                .andExpect(jsonPath("$.bank.name").value("xyz bank"))
                .andExpect(jsonPath("$.bank.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.bank.branchCode").value("6712"))
                .andExpect(jsonPath("$.card",hasSize(2)))
                .andExpect(jsonPath("$..card[0].*",hasSize(3)))
                .andExpect(jsonPath("$.card[1].cardType").value("Credit"))
                .andExpect(jsonPath("$.card[1].cardNumber").value("1234 8273 9384"))
                .andExpect(jsonPath("$.card[1].paymentNetwork").value("Mastero"))
                .andExpect(jsonPath("$.card[0].cardType").value("Debit"))
                .andExpect(jsonPath("$.card[0].cardNumber").value("7653 8273 9384"))
                .andExpect(jsonPath("$.card[0].paymentNetwork").value("VISA"));

    }
    @Test
    public void customerManagementController_getCustomerDetails_qualificationDetails_success() throws Exception {
        QualificationDTO qualification = QualificationDTO.builder()
                .name("Peter Nilson")
                .qualifications(QualificationDTO.Qualifications.builder()
                        .school(QualificationDTO.School.builder()
                                .name("xyz school")
                                .address("Dalaplan Malmo 21744 Sweden")
                                .build())
                        .college(QualificationDTO.College.builder()
                                .name("Abc collage")
                                .address("Abc Malmö 72863 Sweden")
                                .degree("MS")
                                .build())
                        .build())
                .build();
        Mockito.when(customerManagementService.getCustomerQualificationsDetails(Mockito.any())).thenReturn(qualification);
        mockMvc.perform(get("/getDetails/person/12345/request/qualifications")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*",hasSize(2)))
                .andExpect(jsonPath("$.name").value("Peter Nilson"))
                .andExpect(jsonPath("$.qualifications.*",hasSize(2)))
                .andExpect(jsonPath("$.qualifications.school.*",hasSize(2)))
                .andExpect(jsonPath("$.qualifications.college.*",hasSize(3)))
                .andExpect(jsonPath("$.qualifications.school.name").value("xyz school"))
                .andExpect(jsonPath("$.qualifications.school.address").value("Dalaplan Malmo 21744 Sweden"))
                .andExpect(jsonPath("$.qualifications.college.name").value("Abc collage"))
                .andExpect(jsonPath("$.qualifications.college.address").value("Abc Malmö 72863 Sweden"))
                .andExpect(jsonPath("$.qualifications.college.degree").value("MS"));

    }

    @Test
    public void customerManagementController_getCustomerDetails_patchBankDetails_success() throws Exception{

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
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(patchBankDetail );

        CustomerDTO customerObj = CustomerDTO.builder()
                .firstName("John")
                .lastName("Nilson")
                .personId("12345")
                .dob("01/01/1990")
                .streetName("Sodertorp")
                .houseNumber("123")
                .city("Malmö")
                .country("Sweden")
                .countryCode("SE")
                .phone(List.of("+46783946273", "+49763445673", "01248745759", "01234567890"))
                .bank(CustomerDTO.Bank.builder()
                        .bankName("qwerty")
                        .accountNumber("0987654321")
                        .branch("8765")
                        .debitCard(CustomerDTO.DebitCard.builder()
                                .number("0987 4189 9384")
                                .paymentNetwork("Amex")
                                .build())
                        .creditCard(List.of(
                                CustomerDTO.CreditCard.builder()
                                        .number("6789 3245 0967")
                                        .paymentNetwork("VISA")
                                        .build(),
                                CustomerDTO.CreditCard.builder()
                                        .number("0978 4612 8677")
                                        .paymentNetwork("Mastero")
                                        .build()
                        ))
                        .build())
                .education(CustomerDTO.Education.builder()
                        .school(CustomerDTO.School.builder()
                                .name("xyz school")
                                .address("Dalaplan Malmo 21744 Sweden")
                                .build())
                        .college(CustomerDTO.College.builder()
                                .name("Abc collage")
                                .address("Abc Malmö 72863 Sweden")
                                .degree("MS")
                                .build())
                        .build())
                .build();
        Mockito.when(customerManagementService.patchBankDetails(patchBankDetail)).thenReturn(customerObj);
        mockMvc.perform(MockMvcRequestBuilders.
                        patch("/updateDetails/bank")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Nilson"))
                .andExpect(jsonPath("$.phone",hasSize(4)))
                .andExpect(jsonPath("$.bank.creditCard",hasSize(2)))
                .andExpect(jsonPath("$.bank.bankName").value("qwerty"))
                .andExpect(jsonPath("$.bank.branch").value("8765"))
                .andExpect(jsonPath("$.bank.debitCard.number").value("0987 4189 9384"))
                .andExpect(jsonPath("$.bank.debitCard.paymentNetwork").value("Amex"))
                .andExpect(jsonPath("$.education.school.name").value("xyz school"))
                .andExpect(jsonPath("$.education.school.address").value("Dalaplan Malmo 21744 Sweden"))
                .andExpect(jsonPath("$.education.school.*",hasSize(2)))
                .andExpect(jsonPath("$.education.college.*",hasSize(3)))
                .andExpect(jsonPath("$.education.college.name").value("Abc collage"))
                .andExpect(jsonPath("$.education.college.address").value("Abc Malmö 72863 Sweden"))
                .andExpect(jsonPath("$.education.college.degree").value("MS"));

    }
}
