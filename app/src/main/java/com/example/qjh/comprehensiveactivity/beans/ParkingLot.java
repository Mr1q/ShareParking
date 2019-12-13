package com.example.qjh.comprehensiveactivity.beans;

import com.google.gson.annotations.SerializedName;

/**
 * created:qjh
 * time:2019.12.13
 * "park_address": "桂林电子科技大学花江校区",
 * "park_distance": 100,
 * "park_id": 1,
 * "park_latitude": 222222.0,
 * "park_longitude": 111111.0,
 * "park_name": "龙卷风摧毁停车场",
 * "park_ownerId": 10,
 * "park_ownerName": "张三",
 * "park_photoURL": "",
 * "park_price": 500,
 * "park_share": 1,
 * "park_use": 0
 */
public class ParkingLot {

    //  私家车位名称
    @SerializedName("park_name")
    String park_name;

    public String getPark_id() {
        return park_id;
    }

    public void setPark_id(String park_id) {
        this.park_id = park_id;
    }

    //  私家车位ID
    @SerializedName("park_id")
    String park_id;
    // * 私家车位地址
    @SerializedName("park_address")
    String park_address;
    // * 价格
    @SerializedName("park_price")
    int park_price;
    // * 距离
    @SerializedName("park_distance")
    int park_distance;
    // * 持有人姓名
    @SerializedName("park_ownerName")
    String park_ownerName;
    // * 持有人id
    @SerializedName("park_ownerId")
    int park_ownerId;

    // * 是否共享车位
    @SerializedName("park_share")
    String park_share;

    // * 车位状态
    @SerializedName("park_use")
    String park_use;

    //收藏数

    String favoriteNumber;
    @SerializedName("park_longitude")
    String park_longitude; //经度
    @SerializedName("park_latitude")
    String park_latitude; //纬度
    @SerializedName("park_photoURL")
    String parklotImage;  //停车图片

    final public String prefix = "http://120.79.18.242:8080/test";

    public String getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(String favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public String getParklotImage() {
        return prefix + parklotImage;
    }

    public void setParklotImage(String parklotImage) {
        this.parklotImage = parklotImage;
    }


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
