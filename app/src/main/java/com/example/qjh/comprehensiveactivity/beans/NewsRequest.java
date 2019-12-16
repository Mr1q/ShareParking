package com.example.qjh.comprehensiveactivity.beans;

import com.example.qjh.comprehensiveactivity.constant.Constants;

public class NewsRequest {

    @Override
    public String toString() {
        String retValue;
        retValue = "?" + "&username=" +username+ "&password=" + password;
       return retValue;
    }

    public String toStringFindMyPark() {
        String retValue;
        retValue = "?" + "&parkOwnerId=" +parkOwnerId;
        return retValue;
    }
    public String toStringShareParkLot() {
        String retValue;
        retValue = "?longitude="+longitude+"&latitude="+latitude+"&distance="+distance+"&userId="+userId+"&pageNo="+
                pageNo+"&pageSize="+pageSize;
        return retValue;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    private String longitude;  //经度
    private String latitude;   //纬度
    private String distance;  //距离
    private String userId;  //用户ID
    private String pageNo;  //当前第几页
    private String pageSize; //当前页数


    private String username;

    private String password;

    public String getParkOwnerId() {
        return parkOwnerId;
    }

    public void setParkOwnerId(String parkOwnerId) {
        this.parkOwnerId = parkOwnerId;
    }

    private String   parkOwnerId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
