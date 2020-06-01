package com.example.removedragon;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("ID")
    private  String ID;

    @SerializedName("PACKAGE")
    private  String packagName;


    @SerializedName("NAME")
    private  String Name;

    public String getID() {
        return ID;
    }

    public String getPackagName() {
        return packagName;
    }

    public String getName() {
        return Name;
    }
}
