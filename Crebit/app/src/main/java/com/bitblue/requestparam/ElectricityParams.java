package com.bitblue.requestparam;

public class ElectricityParams {
    private String CusAccNum;
    private String BU;
    private String CySubDivision;
    private String DueDate;

    public ElectricityParams(String cusAccNum, String BU, String cySubDivision, String dueDate) {
        CusAccNum = cusAccNum;
        this.BU = BU;
        CySubDivision = cySubDivision;
        DueDate = dueDate;
    }

    public String getCusAccNum() {
        return CusAccNum;
    }

    public String getBU() {
        return BU;
    }

    public String getCySubDivision() {
        return CySubDivision;
    }

    public String getDueDate() {
        return DueDate;
    }
}
