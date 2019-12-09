package com.example.qjh.comprehensiveactivity.beans;

public class ParkingLot {
    //  私家车位名称
    String park_name;
    // * 私家车位地址
    String park_address;
    // * 价格
    int park_price;
    // * 距离
    int park_distance;
    // * 持有人姓名
    String park_ownerName;
    // * 持有人id
    int park_ownerId;

    public String getPark_name() {
        return park_name;
    }

    public void setPark_name(String park_name) {
        this.park_name = park_name;
    }

    public String getPark_address() {
        return park_address;
    }

    public void setPark_address(String park_address) {
        this.park_address = park_address;
    }

    public int getPark_price() {
        return park_price;
    }

    public void setPark_price(int park_price) {
        this.park_price = park_price;
    }

    public int getPark_distance() {
        return park_distance;
    }

    public void setPark_distance(int park_distance) {
        this.park_distance = park_distance;
    }

    public String getPark_ownerName() {
        return park_ownerName;
    }

    public void setPark_ownerName(String park_ownerName) {
        this.park_ownerName = park_ownerName;
    }

    public int getPark_ownerId() {
        return park_ownerId;
    }

    public void setPark_ownerId(int park_ownerId) {
        this.park_ownerId = park_ownerId;
    }

    public double getPark_longitude() {
        return park_longitude;
    }

    public void setPark_longitude(double park_longitude) {
        this.park_longitude = park_longitude;
    }

    public double getPark_latitude() {
        return park_latitude;
    }

    public void setPark_latitude(double park_latitude) {
        this.park_latitude = park_latitude;
    }

    // * 经度
    double park_longitude;
    // * 纬度
    double park_latitude;

}
