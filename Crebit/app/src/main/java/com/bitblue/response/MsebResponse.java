package com.bitblue.response;

public class MsebResponse {
    private int billAmount;
    private String dueDate;
    private int consumptionUnits;

    public MsebResponse(int billAmount, String dueDate, int consumptionUnits) {
        this.billAmount = billAmount;
        this.dueDate = dueDate;
        this.consumptionUnits = consumptionUnits;
    }

    public int getBillAmount() {
        return billAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getConsumptionUnits() {
        return consumptionUnits;
    }
}
