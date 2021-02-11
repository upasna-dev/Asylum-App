package com.traidev.asylum.Utility;


public class User {

    private String mobile;
    private String name;
    private String uid;
    private String city;

    public User(String name,String mobile,String uid,String city)
    {
        this.name = name;
        this.mobile = mobile;
        this.uid = uid;
        this.city = city;
    }


    public String getName() {
        return name;
    }
    public String getMobile() {return mobile;}
    public String getUid() {return uid;}
    public String getCity() {return city;}



}

