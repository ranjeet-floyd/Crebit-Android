package in.crebit.app.WebView.response;

public class ElectricityResponse {
    private String Status;
    private String AvaiBal;

    public ElectricityResponse(String status, String avaiBal) {
        Status = status;
        AvaiBal = avaiBal;
    }

    public String getStatus() {
        return Status;
    }

    public String getAvaiBal() {
        return AvaiBal;
    }
}
