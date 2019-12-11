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

    // * 是否共享车位
    String park_share;

    // * 车位状态
    String park_use;

    //收藏数
    String favoriteNumber;

    public String getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(String favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public String getParklotImage() {
        return parklotImage;
    }

    public void setParklotImage(String parklotImage) {
        this.parklotImage = parklotImage;
    }

    String park_longitude; //经度
    String park_latitude; //纬度
    String parklotImage;  //停车图片

    public String getPark_share() {
        return park_share;
    }

    public String getPark_use() {
        return park_use;
    }

    public String getPark_longitude() {
        return park_longitude;
    }

    public String getPark_latitude() {
        return park_latitude;
    }

    public void setPark_longitude(String park_longitude) {
        this.park_longitude = park_longitude;
    }

    public void setPark_latitude(String park_latitude) {
        this.park_latitude = park_latitude;
    }

    public String getShare() {
        return park_share;
    }

    public void setShare(String share) {
        park_share = share;
    }


    public String getStatus() {
        return park_use;
    }

    public void setStatus(String park_use) {
        this.park_use = park_use;
    }


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



}
