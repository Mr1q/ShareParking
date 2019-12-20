package com.example.qjh.comprehensiveactivity.beans;

import com.google.gson.annotations.SerializedName;

public class Car {
    //  车牌号
    @SerializedName("car_number")
    String carNumber;

    // * 车辆所属位置
    @SerializedName("car_address")
    String carAddress;

    // * 车辆ID
    @SerializedName("car_id")
    String carId;


    // * 是否为默认车辆
    @SerializedName("car_isAlways")
    String isAlways;

    //所属用户ID
    @SerializedName("car_userId")
    String car_userId;

    //车辆图片
    @SerializedName("car_photoURL")
    String car_photoURL;

    final public String prefix = "http://120.79.18.242:8080/test";

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarAddress() {
        return carAddress;
    }

    public void setCarAddress(String carAddress) {
        this.carAddress = carAddress;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getAlways() {
        return isAlways;
    }

    public void setAlways(String always) {
        isAlways = always;
    }

    public String getCar_userId() {
        return car_userId;
    }

    public void setCar_userId(String car_userId) {
        this.car_userId = car_userId;
    }

    public String getCar_photoURL() {
        return prefix+car_photoURL;
    }

    public void setCar_photoURL(String car_photoURL) {
        this.car_photoURL = car_photoURL;
    }
}
