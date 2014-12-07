package com.bitblue.response;

public class TranSumResponse {
    private String CBalance;
    private String profit;
    private String Amount;
    private String Source;
    private String TDate;
    private String Status;
    private String OperaterName;

    public TranSumResponse(String CBalance, String profit, String amount, String source, String TDate, String status, String operaterName) {
        this.CBalance = CBalance;
        this.profit = profit;
        Amount = amount;
        Source = source;
        this.TDate = TDate;
        Status = status;
        OperaterName = operaterName;
    }

    public String getCBalance() {
        return CBalance;
    }

    public String getProfit() {
        return profit;
    }

    public String getAmount() {
        return Amount;
    }

    public String getSource() {
        return Source;
    }

    public String getTDate() {
        return TDate;
    }

    public String getStatus() {
        return Status;
    }

    public String getOperaterName() {
        return OperaterName;
    }
}
