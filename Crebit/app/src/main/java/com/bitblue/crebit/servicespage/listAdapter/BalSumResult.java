package com.bitblue.crebit.servicespage.listAdapter;

public class BalSumResult {
    private String Name;
    private String Amount;
    private String Contact;
    private String Date;
    private int Type;
    private String TransactionId;

    public BalSumResult(String name, String amount, String contact, String date, int type, String transactionId) {
        Name = name;
        Amount = amount;
        Contact = contact;
        Date = date;
        Type = type;
        TransactionId = transactionId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setType(int type) {
        Type = type;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getName() {
        return Name;
    }

    public String getAmount() {
        return Amount;
    }

    public String getContact() {
        return Contact;
    }

    public String getDate() {
        return Date;
    }

    public int getType() {
        return Type;
    }

    public String getTransactionId() {
        return TransactionId;
    }
}