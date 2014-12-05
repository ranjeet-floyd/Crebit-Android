package com.bitblue.response;

public class SignUpResponse {
    private String Status;

    public SignUpResponse(String status) {
        Status = status;
    }

    public int getStatus() {
        return Integer.parseInt(Status);
    }
}
