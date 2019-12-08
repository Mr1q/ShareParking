package com.example.qjh.comprehensiveactivity.beans;

public class Car {
    //  车牌号
    String carNumber;
    // * 车辆所属位置
    String carAddress;
    // * 是否为默认车辆
    Boolean isAlways;

    public  Car(String carNumber,String carAddress,Boolean isAlways)
    {
        this.carAddress=carAddress;
        this.carNumber=carNumber;
        this.isAlways=isAlways;
    }

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

    public Boolean getAlways() {
        return isAlways;
    }

    public void setAlways(Boolean always) {
        this.isAlways = always;
    }
}
