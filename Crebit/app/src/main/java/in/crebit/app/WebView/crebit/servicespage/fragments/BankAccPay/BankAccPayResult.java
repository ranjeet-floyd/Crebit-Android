package in.crebit.app.WebView.crebit.servicespage.fragments.BankAccPay;

public class BankAccPayResult {
    private String count;
    private String name;
    private String account;
    private String iFSC;
    private String mobile;
    private String amount;
    private String status;

    public BankAccPayResult() {
    }

    public BankAccPayResult(String count, String name, String account, String iFSC, String mobile, String amount,String status) {
        this.count = count;
        this.name = name;
        this.account = account;
        this.iFSC = iFSC;
        this.mobile = mobile;
        this.amount = amount;
        this.status = status;
    }

    public String getCount() {
        return count;
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

    public String getStatus() {
        return status;
    }

    public void setCount(String count) {
        this.count = count;
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

    public void setStatus(String status) {
        this.status = status;
    }

}
