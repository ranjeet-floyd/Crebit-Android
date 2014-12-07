package com.bitblue.requestparam;

public class InsuranceParams {
    private String UserId;
    private String Key;
    private String TransactionType;
    private String OperatorId;
    private String Number;
    private double Amount;
    private String Source;
    private String InsuranceDob;

    public InsuranceParams(String userId, String key, String transactionType, String operatorId, String number, double amount, String source, String insuranceDob) {
        UserId = userId;
        Key = key;
        TransactionType = transactionType;
        OperatorId = operatorId;
        Number = number;
        Amount = amount;
        Source = source;
        InsuranceDob = insuranceDob;
    }

    public String getUserId() {
        return UserId;
    }

    public String getKey() {
        return Key;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public String getOperatorId() {
        return OperatorId;
    }

    public String getNumber() {
        return Number;
    }

    public double getAmount() {
        return Amount;
    }

    public String getSource() {
        return Source;
    }

    public String getInsuranceDob() {
        return InsuranceDob;
    }
}
