package com.bank.System_V1.services.impl;

import com.bank.System_V1.dto.*;
import com.bank.System_V1.entity.User;
import com.bank.System_V1.repository.userRepository;
import com.bank.System_V1.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    userRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        /**
         * Creating an account - saving a new user into db
         * check if user already has an account
         */

        if(userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .message(AccountUtils.Account_Already_Exists_Message)
                    .code(AccountUtils.Account_Already_Exists_Code)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .status("Active")
                .build();

        User savedUser = userRepository.save(newUser);
        EmailDetails ed = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation in Banking System")
                .message("Congratulations your account are now activated.\nYour account details : \n" +
                        "Account Name : " + savedUser.getFirstName() + " " + savedUser.getLastName() +
                        "\nAccount Number : " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(ed);

        return BankResponse.builder()
                .code(AccountUtils.Account_Creation_Code)
                .message(AccountUtils.Account_Creation_Success_Message)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }


    /**
     * balance enquiry, name enquiry, credit, debit, transfer
     * */

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if provided account number exist
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist) {
                return BankResponse.builder()
                        .code(AccountUtils.Account_Not_Exist_code)
                        .message(AccountUtils.Account_Not_Exist_message)
                        .accountInfo(null)
                        .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .code(AccountUtils.Account_Exist_code)
                .message(AccountUtils.Account_Exist_Message)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist) {
            return AccountUtils.Account_Not_Exist_message;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        // check if the account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExist) {
            return BankResponse.builder()
                    .code(AccountUtils.Account_Not_Exist_code)
                    .message(AccountUtils.Account_Not_Exist_message)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);

//        EmailDetails ed = EmailDetails.builder()
//                .recipient(userToCredit.getEmail())
//                .subject("Credit Request")
//                .message("Credit Done Successfully with amount : " + creditDebitRequest.getAmount()
//                + "\nYour Currrent balance : " + userToCredit.getAccountBalance())
//                .build();
//        emailService.sendEmailAlert(ed);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditDebitRequest.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .code(AccountUtils.Account_Credited_Code)
                .message(AccountUtils.Account_Credited_Success_Message)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExist) {
            return BankResponse.builder()
                    .code(AccountUtils.Account_Not_Exist_code)
                    .message(AccountUtils.Account_Not_Exist_message)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        if(userToDebit.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .code(AccountUtils.Insufficent_Balance_Code)
                    .message(AccountUtils.Insufficent_Balance_Message)
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        userRepository.save(userToDebit);

//        EmailDetails ed = EmailDetails.builder()
//                .recipient(userToDebit.getEmail())
//                .subject("Debit Request")
//                .message("Debit Done Successfully with amount : " + creditDebitRequest.getAmount()
//                        + "\nYour Currrent balance : " + userToDebit.getAccountBalance())
//                .build();
//        emailService.sendEmailAlert(ed);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(creditDebitRequest.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .code(AccountUtils.Debit_Success_Code)
                .message(AccountUtils.Debit_Success_Message)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        Boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccount());
        Boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccount());
        if(!isDestinationAccountExist || !isSourceAccountExist) {
            return BankResponse.builder()
                    .code(AccountUtils.Account_Not_Exist_code)
                    .message(AccountUtils.Account_Not_Exist_message)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getSourceAccount());
        User userToCredit = userRepository.findByAccountNumber(request.getDestinationAccount());

        if(userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .code(AccountUtils.Insufficent_Balance_Code)
                    .message(AccountUtils.Insufficent_Balance_Message)
                    .accountInfo(null)
                    .build();
        }

        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

        userRepository.save(userToDebit);
        userRepository.save(userToCredit);

        EmailDetails DebitMail = EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("Debit Alert")
                .message("The sum of " + request.getAmount() + "$ Has been deducted from your account!"+
                        "\nSent to " + userToCredit.getEmail() +
                        "\nYour current Balance is : " + userToDebit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(DebitMail);

        EmailDetails CreditMail = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("Credit Alert")
                .message("The sum of " + request.getAmount() + "$ Has been sent to your account from "
                        + userToDebit.getEmail() + " : " + userToDebit.getFirstName() + " " + userToDebit.getLastName() +
                        "\nYour current Balance is : " + userToDebit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(CreditMail);

        TransactionDto transactionDto1 = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto1);

        TransactionDto transactionDto2 = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto2);

        return BankResponse.builder()
                .code(AccountUtils.Transfer_Success_code)
                .message(AccountUtils.Transfer_Success_Message)
                .accountInfo(null)
                .build();
    }


}
