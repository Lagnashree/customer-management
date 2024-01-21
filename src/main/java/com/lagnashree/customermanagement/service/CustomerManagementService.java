package com.lagnashree.customermanagement.service;
import com.lagnashree.customermanagement.dto.*;
import com.lagnashree.customermanagement.exception.InternalException;
import com.lagnashree.customermanagement.exception.InvalidInputException;
import com.lagnashree.customermanagement.model.Customer;
import com.lagnashree.customermanagement.repository.CustomerManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            Customer customerObj = customerManagementRepository.readCustomerData();

            if(!personId.equals(customerObj.getPersonId())) {
                log.error(String.format("Invalid person id: %s", personId));
                throw new InvalidInputException("invalid person id");
            }
            else {
                PersonalDetailDTO personalDTO = PersonalDetailDTO.builder()
                        .name( customerObj.getFirstName() + " " + customerObj.getLastName())
                        .personId((String) customerObj.getPersonId())
                        .dob((String) customerObj.getDob())
                        .address(PersonalDetailDTO.Address.builder()
                                .street((String) customerObj.getStreetName())
                                .houseNumber((String) customerObj.getHouseNumber())
                                .city((String) customerObj.getCity())
                                .country((String) customerObj.getCountry())
                                .countryCode((String) customerObj.getCountryCode()).build())
                        .contact(generateContacts(customerObj.getPhone()))
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
    private static List<PersonalDetailDTO.Contact> generateContacts(List<String> phoneArray) {

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
            Customer customerObj = customerManagementRepository.readCustomerData();
            /*Bank bankObject = (JSONObject) customerObj.get("bank");
            JSONObject debitCardObject = (JSONObject) bankObject.get("debitCard");
            JSONObject creditcardObject = (JSONObject) bankObject.get("creditCard");*/
            if(!personId.equals(customerObj.getPersonId())) {
                log.error(String.format("Invalid person id: %s", personId));
                throw new InvalidInputException("invalid person id");
            }
            else {
                BankDetailDTO bankDetailDTO = BankDetailDTO.builder()
                        .name( customerObj.getFirstName() + " " +  customerObj.getLastName())
                        .personId(customerObj.getPersonId())
                        .bank(BankDetailDTO.Bank.builder()
                                .name( customerObj.getBank().getBankName())
                                .accountNumber( customerObj.getBank().getAccountNumber())
                                .branchCode((String) customerObj.getBank().getBranch()).build())
                        .card(List.of(
                                BankDetailDTO.Card.builder()
                                        .cardType("Credit")
                                        .cardNumber(customerObj.getBank().getCreditCard().getNumber())
                                        .paymentNetwork(customerObj.getBank().getCreditCard().getPaymentNetwork())
                                        .build(),
                                BankDetailDTO.Card.builder()
                                        .cardType("Debit")
                                        .cardNumber((String) customerObj.getBank().getDebitCard().getNumber())
                                        .paymentNetwork((String) customerObj.getBank().getDebitCard().getPaymentNetwork())
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
            Customer customerObj = customerManagementRepository.readCustomerData();
            if(!personId.equals(customerObj.getPersonId())) {
                log.error(String.format("Invalid person id: %s", personId));
                throw new InvalidInputException("invalid person id");
            }
            else {
                QualificationDTO qualificationDTO = QualificationDTO.builder()
                        .name(customerObj.getFirstName() + " " + customerObj.getFirstName())
                        .qualifications(QualificationDTO.Qualifications.builder()
                                .school(QualificationDTO.School.builder()
                                        .name(customerObj.getEducation().getSchool().getName() )
                                        .address(customerObj.getEducation().getSchool().getAddress())
                                        .build())
                                .college(QualificationDTO.College.builder()
                                        .name( customerObj.getEducation().getCollage().getName())
                                        .address(customerObj.getEducation().getCollage().getAddress())
                                        .degree( customerObj.getEducation().getCollage().getDegree())
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
            Customer customerObj = customerManagementRepository.readCustomerData();
            if(!pathchBankDetailDTO.getPersonId().equals((String) customerObj.getPersonId())) {
                log.error(String.format("Invalid person id: %s", pathchBankDetailDTO.getPersonId()));
                throw new InvalidInputException("invalid person id");
            }
            else {
                CustomerDTO customerDTO = CustomerDTO.builder()
                        .firstName( customerObj.getFirstName())
                        .lastName((String) customerObj.getLastName())
                        .personId((String) customerObj.getPersonId())
                        .dob((String) customerObj.getDob())
                        .streetName((String) customerObj.getStreetName())
                        .houseNumber((String) customerObj.getHouseNumber())
                        .city((String) customerObj.getCity())
                        .country((String) customerObj.getCountry())
                        .countryCode((String) customerObj.getCountryCode())
                        .phone(generatePhonenumbers(customerObj.getPhone()))
                        .bank(generateBankDetails(pathchBankDetailDTO))
                        .education(CustomerDTO.Education.builder()
                                .school(CustomerDTO.School.builder()
                                        .name(customerObj.getEducation().getCollage().getName() )
                                        .address( customerObj.getEducation().getCollage().getAddress())
                                        .build())
                                .college(CustomerDTO.College.builder()
                                        .name(customerObj.getEducation().getCollage().getName())
                                        .address(customerObj.getEducation().getCollage().getAddress())
                                        .degree(customerObj.getEducation().getCollage().getDegree())
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
    private static List<String> generatePhonenumbers(List<String> phoneArray) {

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
