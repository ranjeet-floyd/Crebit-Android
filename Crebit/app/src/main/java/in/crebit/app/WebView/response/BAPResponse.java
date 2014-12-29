package in.crebit.app.WebView.response;

public class BAPResponse {
    private int id;
    private String name;
    private String account;
    private String iFSC;
    private String mobile;
    private String amount;

    public BAPResponse(int id, String name, String account, String iFSC, String mobile, String amount) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.iFSC = iFSC;
        this.mobile = mobile;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getiFSC() {
        return iFSC;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setiFSC(String iFSC) {
        this.iFSC = iFSC;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
