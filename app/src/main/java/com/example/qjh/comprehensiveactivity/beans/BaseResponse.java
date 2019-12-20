package com.example.qjh.comprehensiveactivity.beans;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    private String msg;
    private int state;
    public final static int RESPONSE_SUCCESS = 0;
    @SerializedName("parkData")
    private T data;

    @SerializedName("userData")
    private T userData;

    @SerializedName("data") //返回私家车辆信息
    private T carData;

    public T getCarData() {
        return carData;
    }

    public void setCarData(T carData) {
        this.carData = carData;
    }

    public BaseResponse() {
    }

    public T getUserData() {
        return userData;
    }

    public void setUserData(T userData) {
        this.userData = userData;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
