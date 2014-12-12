package com.bitblue.IDs;

public class type {
    private static int typeID;

    public static int getTypeId(int position) {
        switch (position) {
            case 0:
                typeID = -1;
                break;
            case 1:
                typeID = 1;
                break;
            case 2:
                typeID = 2;
                break;
            case 3:
                typeID = 3;
                break;
            case 4:
                typeID = 4;
                break;
            case 5:
                typeID = 5;
                break;
            case 6:
                typeID = 6;
                break;
            case 7:
                typeID = 7;
                break;
            case 8:
                typeID = 8;
                break;
            case 9:
                typeID = 9;
                break;
            case 10:
                typeID = 10;
                break;
            case 11:
                typeID = 11;
                break;
            case 12:
                typeID = 12;
                break;
            case 13:
                typeID = 13;
                break;
            case 14:
                typeID = 14;
            default:
                typeID = -1;
                break;

        }
        return typeID;
    }
}
