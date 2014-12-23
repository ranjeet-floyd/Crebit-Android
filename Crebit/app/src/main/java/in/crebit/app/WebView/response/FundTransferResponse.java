package in.crebit.app.WebView.response;

public class FundTransferResponse {
    private String Status;
    private String AvailableBalance;

    public FundTransferResponse(String status, String availableBalance) {
        Status = status;
        AvailableBalance = availableBalance;
    }

    public String getStatus() {
        return Status;
    }

    public String getAvailableBalance() {
        return AvailableBalance;
    }
}
