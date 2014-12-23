package in.crebit.app.WebView.crebit.servicespage.fragments.updates;


//[{"text":"Send Money To Anywhere Across India \n Upto RS :5000   Charges -1%\nAnd above 5000 Charges -0.5%\nTiming Monday to Friday \n9.00 Am - 6:00 pm \nsaturday  9:00 Am - 4:00 Pm \n","fDate":"2014-11-17T19:25:25.12","tDate":"2014-11-30T19:25:25.12"}
// ,{"text":"Utility Bill Rs : 5 Charges\nhttps://www.facebook.com/\nCrebitwalletSupport@crebit.in\n 0250-3297961\n Monday - Friday\n 10:00am to 4:30pm","fDate":"2014-11-17T19:25:25.12","tDate":"2014-11-30T19:25:25.14"}
// {"text":"Create Presonal Account and Get Rs 10 Cash Back ","fDate":"2014-11-18T00:00:00","tDate":"2014-12-01T00:00:00"}]

public class UpdateResult {
    private String count;
    private String text;
    private String fDate;
    private String tDate;

    public UpdateResult(String count, String text, String fDate, String tDate) {
        this.count = count;
        this.text = text;
        this.fDate = fDate;
        this.tDate = tDate;
    }

    public String getCount() {
        return count;
    }

    public String getText() {
        return text;
    }

    public String getfDate() {
        return fDate;
    }

    public String gettDate() {
        return tDate;
    }
}
