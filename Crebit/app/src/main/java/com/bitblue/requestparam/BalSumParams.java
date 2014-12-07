package com.bitblue.requestparam;

public class BalSumParams {
    private String UserId;
    private String Key;
    private String FromDate;
    private String ToDate;

    public BalSumParams(String userId, String key, String fromDate, String toDate) {
        UserId = userId;
        Key = key;
        FromDate = fromDate;
        ToDate = toDate;
    }

    public String getUserId() {
        return UserId;
    }

    public String getKey() {
        return Key;
    }

    public String getFromDate() {
        return FromDate;
    }

    public String getToDate() {
        return ToDate;
    }
}
