package com.example.qjh.comprehensiveactivity.beans;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("addTime")
    private String payaddtime;

    //停车场名字
    private String parklotName;

    @SerializedName("carNumber")
    private String carNumber;

    @SerializedName("carId")
    private String carId;

    @SerializedName("address")
    private String payaddress;


    @SerializedName("paymoney")
    private String paymoney;



}
