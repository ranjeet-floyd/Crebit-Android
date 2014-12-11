package com.bitblue.nullcheck;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static boolean ifAccountNumberIncorrect(String number) {
        return (number.length() > 12 || number.length() < 12 || number.equals(""));
    }

    public static boolean ifTodayLessThanDue(String Duedate) {
        Date nowdate = new Date();
        Date due = null, now = null;
        String today = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try {
            due = sdf.parse(Duedate);   //Convert 10 DEC 2014 (String)into(date Object)
            today = sdf.format(nowdate);  //Convert todays(date object) into (string) of type (DD MM YYYY)
            now = sdf.parse(today);       //Convert the (String) into todays (date object)
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (now.before(due)) {       //compare two dates
            return true;
        }
        return false;
    }


}
