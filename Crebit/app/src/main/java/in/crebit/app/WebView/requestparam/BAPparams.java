package in.crebit.app.WebView.requestparam;

public class BAPparams {
   private String password;
   private String userId;

    public BAPparams(String password, String userId) {
        this.password = password;
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }
}
