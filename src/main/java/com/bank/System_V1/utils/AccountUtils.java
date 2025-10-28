package com.bank.System_V1.utils;

import java.time.Year;
import java.util.Random;

public class AccountUtils {

    public static final String Account_Already_Exists_Code = "001";
    public static final String Account_Already_Exists_Message = "This user already has an account ";

    public static final String Account_Creation_Code = "002";
    public static final String Account_Creation_Success_Message = "Account created successfully";

    public static final String Account_Not_Exist_code = "003";
    public static final String Account_Not_Exist_message = "There is no account with this account number";

    public static final String Account_Exist_code = "004";
    public static final String Account_Exist_Message = "Account found successfully ";

    public static final String Account_Credited_Code = "005";
    public static final String Account_Credited_Success_Message = "Credit successfully";

    public static final String Debit_Success_Code = "006";
    public static final String Debit_Success_Message = "Amount debit successfully";

    public static final String Insufficent_Balance_Code = "007";
    public static final String Insufficent_Balance_Message = "Amount exceeds the limit ";

    public static final String Transfer_Success_code = "008";
    public static final String Transfer_Success_Message = "Transfer Done Successful";



    /**
     * 2025 + random 6 digits
     */
    public static String generateAccountNumber() {
        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;

        Random random = new Random();
        int randomNum = random.nextInt(min, max);

        String year = String.valueOf(currentYear);
        String randomValue = String.valueOf(randomNum);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomValue).toString();
    }

}
