package com.lagnashree.customermanagement.service;
import com.lagnashree.customermanagement.dto.*;
import com.lagnashree.customermanagement.exception.InternalException;
import com.lagnashree.customermanagement.exception.InvalidInputException;
import com.lagnashree.customermanagement.repository.CustomerManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j()
public class CustomerManagementService {

    private final CustomerManagementRepository customerManagementRepository;
    /**
     * This function get the customer personal details of a given personId.
     * @param String, personId of the customer
     * @return PersonalDetailDTO object which contains personal details of a customer.
     * @throws InvalidInputException If the provided personId does not match any exsiting data
     * @throws InternalException when there any other king of error while parsing etc.
     */
    public PersonalDetailDTO getCustomerPersonalDetails(String personId) throws InvalidInputException, InternalException  {
        try {
            JSONObject customerObj = customerManagementRepository.readCustomerData();
            JSONArray phoneArray = (JSONArray) customerObj.get("phone");
            if(!personId.equals(customerObj.get("personId"))) {
                log.error(String.format("Invalid person id: %s", personId));
                throw new InvalidInputException("invalid person id");
            }
            else {
                PersonalDetailDTO personalDTO = PersonalDetailDTO.builder()
                        .name( customerObj.get("firstName") + " " + (String) customerObj.get("lastName"))
                        .personId((String) customerObj.get("personId"))
                        .dob((String) customerObj.get("dob"))
                        .address(PersonalDetailDTO.Address.builder()
                                .street((String) customerObj.get("streetName"))
                                .houseNumber((String) customerObj.get("houseNumber"))
                                .city((String) customerObj.get("city"))
                                .country((String) customerObj.get("country"))
                                .countryCode((String) customerObj.get("countryCode")).build())
                        .contact(generateContacts(phoneArray))
                        .build();

                return personalDTO;
            }
        }
        catch(InvalidInputException exp){
            throw exp;
        }
        catch(Exception e){
            log.error(String.format("Exception in getCustomerPersonalDetails function: %s", e.getMessage()), e);
            throw new InternalException("Internal Server Error");
        }
    }

    /**
     * This function process JSONArray object that contains phone numbers, idenitfy the mobile phone number and returns them  .
     * @parameter JSONArray object which contains phone numbers of a customer.
     * @return List of PersonalDetailDTO.Contact
     */
    private static List<PersonalDetailDTO.Contact> generateContacts(JSONArray phoneArray) {

        List<PersonalDetailDTO.Contact> contactList = new ArrayList<>();

        for (int i = 0; i < phoneArray.size(); i++) {
            String phoneNumber = (String) phoneArray.get(i);
               if (phoneNumber.startsWith("+")) {
                   int splitIndex=3;
                   String countryCode = phoneNumber.substring(0, splitIndex);
                   String numberWithoutCountryCode = phoneNumber.substring(splitIndex);
                   PersonalDetailDTO.Contact contact = PersonalDetailDTO.Contact.builder()
                           .code(countryCode)
                           .number(numberWithoutCountryCode)
                           .build();
                   contactList.add(contact);
                }

            }
        return contactList;
    }

    /**
     * This function get the customer bankd details of a given personId.
     * @param String, personId of the customer
     * @return PersonalDetailDTO object which contains personal details of a customer.
     * @throws InvalidInputException If the provided personId does not match any exsiting data
     * @throws InternalException when there any other king of error while parsing etc.
     */
    public BankDetailDTO getCustomerBankDetails(String personId) throws InvalidInputException, InternalException {
        try{
            JSONObject customerObj = customerManagementRepository.readCustomerData();
            JSONObject bankObject = (JSONObject) customerObj.get("bank");
            JSONObject debitCardObject = (JSONObject) bankObject.get("debitCard");
            JSONObject creditcardObject = (JSONObject) bankObject.get("creditCard");
            if(!personId.equals(customerObj.get("personId"))) {
                log.error(String.format("Invalid person id: %s", personId));
                throw new InvalidInputException("invalid person id");
            }
            else {
                BankDetailDTO bankDetailDTO = BankDetailDTO.builder()
                        .name((String) customerObj.get("firstName") + " " + (String) customerObj.get("lastName"))
                        .personId((String) customerObj.get("personId"))
                        .bank(BankDetailDTO.Bank.builder()
                                .name((String) bankObject.get("bankName"))
                                .accountNumber((String) bankObject.get("accountNumber"))
                                .branchCode((String) bankObject.get("branch")).build())
                        .card(List.of(
                                BankDetailDTO.Card.builder()
                                        .cardType("Credit")
                                        .cardNumber((String) creditcardObject.get("number"))
                                        .paymentNetwork((String) creditcardObject.get("paymentNetwork"))
                                        .build(),
                                BankDetailDTO.Card.builder()
                                        .cardType("Debit")
                                        .cardNumber((String) debitCardObject.get("number"))
                                        .paymentNetwork((String) debitCardObject.get("paymentNetwork"))
                                        .build()
                        )).build();
                return bankDetailDTO;
            }
        }
        catch(InvalidInputException exp){
            throw exp;
        }
        catch(Exception e){
            log.error(String.format("Exception in getCustomerBankDetails function: %s", e.getMessage()), e);
            throw new InternalException("Internal Server Error");
        }
    }


    /**
     * This function get the customer qualification details of a given personId.
     * @param String, personId of the customer
     * @return PersonalDetailDTO object which contains personal details of a customer.
     * @throws InvalidInputException If the provided personId does not match any exsiting data
     * @throws InternalException when there any other king of error while parsing etc.
     */
   public QualificationDTO getCustomerQualificationsDetails(String personId) throws InvalidInputException, InternalException {
       try{
           JSONObject customerObj = customerManagementRepository.readCustomerData();
           JSONObject educationObject = (JSONObject) customerObj.get("education");
           JSONObject schoolObject = (JSONObject) educationObject.get("school");
           JSONObject collageObject = (JSONObject) educationObject.get("collage");
           if(!personId.equals(customerObj.get("personId"))) {
               log.error(String.format("Invalid person id: %s", personId));
               throw new InvalidInputException("invalid person id");
           }
           else {
               QualificationDTO qualificationDTO = QualificationDTO.builder()
                       .name(customerObj.get("firstName") + " " + customerObj.get("lastName"))
                       .qualifications(QualificationDTO.Qualifications.builder()
                               .school(QualificationDTO.School.builder()
                                       .name((String) schoolObject.get("name"))
                                       .address((String) schoolObject.get("address"))
                                       .build())
                               .college(QualificationDTO.College.builder()
                                       .name((String) collageObject.get("name"))
                                       .address((String) collageObject.get("address"))
                                       .degree((String) collageObject.get("degree"))
                                       .build())
                               .build()).build();

               return qualificationDTO;
           }
       }
       catch(InvalidInputException exp){
           throw exp;
       }
       catch(Exception e){
           log.error(String.format("Exception in getCustomerQualificationsDetails function: %s", e.getMessage()), e);
           throw new InternalException("Internal Server Error");
       }
    }

    /**
     * This function returns merged customer data based on the input and exsiting customer data.
     * @param PatchBankDetailDTO, data to be merged on exisiting customer data
     * @return PersonalDetailDTO object which contains personal details of a customer.
     * @throws InvalidInputException If the provided personId does not match any exsiting data
     * @throws InternalException when there any other king of error while parsing etc.
     */
    public CustomerDTO patchBankDetails(PatchBankDetailDTO pathchBankDetailDTO) throws InvalidInputException, InternalException {
        try{
            JSONObject customerObj = customerManagementRepository.readCustomerData();
            if(!pathchBankDetailDTO.getPersonId().equals((String) customerObj.get("personId"))) {
                log.error(String.format("Invalid person id: %s", pathchBankDetailDTO.getPersonId()));
                throw new InvalidInputException("invalid person id");
            }
            else {
                JSONObject educationObject = (JSONObject) customerObj.get("education");
                JSONObject schoolObject = (JSONObject) educationObject.get("school");
                JSONObject collageObject = (JSONObject) educationObject.get("collage");
                JSONArray phoneArray = (JSONArray) customerObj.get("phone");
                JSONObject bankObject = (JSONObject) customerObj.get("bank");
                CustomerDTO customerDTO = CustomerDTO.builder()
                        .firstName((String) customerObj.get("firstName"))
                        .lastName((String) customerObj.get("lastName"))
                        .personId((String) customerObj.get("personId"))
                        .dob((String) customerObj.get("dob"))
                        .streetName((String) customerObj.get("streetName"))
                        .houseNumber((String) customerObj.get("houseNumber"))
                        .city((String) customerObj.get("city"))
                        .country((String) customerObj.get("country"))
                        .countryCode((String) customerObj.get("countryCode"))
                        .phone(generatePhonenumbers(phoneArray))
                        .bank(generateBankDetails(pathchBankDetailDTO))
                        .education(CustomerDTO.Education.builder()
                                .school(CustomerDTO.School.builder()
                                        .name((String) schoolObject.get("name"))
                                        .address((String) schoolObject.get("address"))
                                        .build())
                                .college(CustomerDTO.College.builder()
                                        .name((String) collageObject.get("name"))
                                        .address((String) collageObject.get("address"))
                                        .degree((String) collageObject.get("degree"))
                                        .build())
                                .build()).build();

                return customerDTO;
            }
        }
        catch(InvalidInputException exp){
            throw exp;
        }
        catch(Exception e){
            log.error(String.format("Exception in patchBankDetails function: %s", e.getMessage()), e);
            throw new InternalException("Internal Server Error");
        }
    }

    /**
     * This function process JSONArray object that contains phone numbers, and convert them into List of String and return  .
     * @parameter JSONArray object which contains phone numbers of a customer.
     * @return List of String
     */
    private static List<String> generatePhonenumbers(JSONArray phoneArray) {

        List<String> contactList = new ArrayList<>();

        for (int i = 0; i < phoneArray.size(); i++) {
            String phoneNumber = (String) phoneArray.get(i);
            contactList.add(phoneNumber);
        }
        return contactList;
    }

    /**
     * This function process JSONArray object that contains phone numbers, and convert them into List of String and return  .
     * @parameter PatchBankDetailDTO object which contains phone numbers of a customer.
     * @return CustomerDTO.Bank
     */
    private static CustomerDTO.Bank generateBankDetails(PatchBankDetailDTO pathchBankDetailDTO) {

        CustomerDTO.Bank bankDetais= CustomerDTO.Bank.builder()
                .bankName(pathchBankDetailDTO.getNewBankDetails().getName())
                .branch(pathchBankDetailDTO.getNewBankDetails().getBranch())
                .accountNumber(pathchBankDetailDTO.getNewBankDetails().getAccount())
                .debitCard(CustomerDTO.DebitCard.builder()
                        .number(pathchBankDetailDTO.getNewBankDetails().getDebitCard().getNumber())
                        .paymentNetwork(pathchBankDetailDTO.getNewBankDetails().getDebitCard().getPaymentNetwork()).build())
                .creditCard(generateCreditCards(pathchBankDetailDTO.getCreditCards())).build();

        return bankDetais;
    }

    /**
     * This function process JSONArray object that contains phone numbers, and convert them into List of String and return  .
     * @parameter List of PatchBankDetailDTO.CreditCard
     * @return List of CustomerDTO.CreditCard
     */
    private static List<CustomerDTO.CreditCard> generateCreditCards(List<PatchBankDetailDTO.CreditCard> creditCardInfo) {

        List<CustomerDTO.CreditCard> creditCardList= new ArrayList<>();
        for (PatchBankDetailDTO.CreditCard creditCard : creditCardInfo) {
            CustomerDTO.CreditCard creditCardObj= CustomerDTO.CreditCard.builder()
                    .number(creditCard.getNumber())
                    .paymentNetwork(creditCard.getPaymentNetwork()).build();
            creditCardList.add(creditCardObj);
        }
        return creditCardList;
    }
}


