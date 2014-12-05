package com.bitblue.requestparam;

public class SignUpParams {
    private String UserType;
    private String Name;
    private String Pass;
    private String Mobile;

    public SignUpParams(String userType, String name, String pass, String mobile) {
        UserType = userType;
        Name = name;
        Pass = pass;
        Mobile = mobile;
    }

    public String getUserType() {
        return UserType;
    }

    public String getName() {
        return Name;
    }

    public String getPass() {
        return Pass;
    }

    public String getMobile() {
        return Mobile;
    }
}
