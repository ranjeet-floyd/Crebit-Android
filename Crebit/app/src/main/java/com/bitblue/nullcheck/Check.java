package com.bitblue.nullcheck;

public class Check {
    public static boolean ifNull(String var) {
        return (var.equals("") || var.equals("--Select--"));
    }

    public static boolean ifEmpty(double amount) {
        return (String.valueOf(amount).equals("") || (amount == 0));
    }

    public static boolean ifNumberInCorrect(String number) {
        return (number.length() > 10 || number.length() < 10 || number.equals(""));

    }
}
