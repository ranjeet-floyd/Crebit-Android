package com.bitblue.crebit.services;


public class DrawerItem {
    String ItemName;
    int imgResID;

    public DrawerItem(String itemName, int imgResID) {
        this.ItemName = itemName;
        this.imgResID = imgResID;
    }

    public int getImgResID() {
        return imgResID;
    }

    public String getItemName() {
        return ItemName;
    }
}
