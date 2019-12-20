package com.example.qjh.comprehensiveactivity.beans;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    //订单号
    @SerializedName("id")
    private String id;

    //停车场名字
    @SerializedName("status")
    private String status;

    @SerializedName("carNumber") //车牌号信息
    private String carNumber;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SerializedName("carId") //车位ID
    private String carId;

    //收费标准
    @SerializedName("price")
    private String price;


    @SerializedName("amount")  //总价格
    private String amount;
    @SerializedName("userId")  //用户ID
    private String userId;


}
