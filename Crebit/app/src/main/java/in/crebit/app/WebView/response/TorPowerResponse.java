package in.crebit.app.WebView.response;

public class TorPowerResponse {
    private int Status;
    private String Message;
    private int AvaiBal;

    public TorPowerResponse(int status, String message, int avaiBal) {
        Status = status;
        Message = message;
        AvaiBal = avaiBal;
    }

    public int getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public int getAvaiBal() {
        return AvaiBal;
    }
}
