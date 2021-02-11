package com.traidev.asylum;

import com.google.gson.annotations.SerializedName;

public class DoctorViewModal {

    @SerializedName("id")
    private String ID;

    @SerializedName("name")
    private String name;

    @SerializedName("spec")
    private String spec;

    @SerializedName("address")
    private String Address;

    @SerializedName("profile")
    private String Profile;

    public String getID() {
        return ID;
    }


    public String getName() {
        return name;
    }

    public String getSpec() {
        return spec;
    }

    public String getAddress() {
        return Address;
    }

    public String getProfile() {
        return Profile;
    }
}
