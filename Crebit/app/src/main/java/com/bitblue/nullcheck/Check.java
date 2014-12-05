package com.bitblue.nullcheck;

public class Check {
    public static boolean ifNull(String var) {
        return var.equals("");
    }

    public static boolean ifNumberInCorrect(String number) {
        return (number.length() > 10 || number.length() < 10);

    }
}
