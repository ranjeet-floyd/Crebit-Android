package com.bitblue.IDs;

public class status {
    private static int statusID;

    public static int getStatusId(int position) {
        switch (position) {
            case 0:
                statusID = -1;
                break;
            case 1:
                statusID = 0;
                break;
            case 2:
                statusID = 1;
                break;
            case 3:
                statusID = 2;
                break;
            case 4:
                statusID = 3;
                break;
            case 5:
                statusID = 4;
                break;
            case 6:
                statusID = 5;
                break;
            case 7:
                statusID = 6;
                break;
            case 8:
                statusID = 7;
                break;
            case 9:
                statusID = 8;
                break;
            case 10:
                statusID = 9;
            default:
                statusID = -1;
        }
        return statusID;
    }
}
