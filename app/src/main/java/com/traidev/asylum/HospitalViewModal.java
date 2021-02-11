package com.traidev.asylum;

import com.google.gson.annotations.SerializedName;

public class HospitalViewModal {

    @SerializedName("id")
    private String ID;

    @SerializedName("title")
    private String Title;

    @SerializedName("rating")
    private String Rating;

    @SerializedName("thumb")
    private String Banner;

    public String getID() {
        return ID;
    }


    public String getTitle() {
        return Title;
    }


    public String getRating() {
        return Rating;
    }



    public String getBanner() {
        return Banner;
    }


}
