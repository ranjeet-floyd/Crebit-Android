package in.crebit.app.WebView.requestparam;

public class PostPaidParams {
    private String UserId;
    private String Key;
    private String OperatorId;
    private String Number;
    private double Amount;
    private String Source;

    public PostPaidParams(String userId, String key, String operatorId, String number, double amount, String source) {
        UserId = userId;
        Key = key;
        OperatorId = operatorId;
        Number = number;
        Amount = amount;
        Source = source;
    }

    public String getUserId() {
        return UserId;
    }

    public String getKey() {
        return Key;
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
}


