package com.example.qjh.comprehensiveactivity.beans;

import com.google.gson.annotations.SerializedName;

public class Comment {
    //  评论内容
    @SerializedName("content")
    String content;

    // * 评论ID
    @SerializedName("id")
    String id;

    // * 车位ID
    @SerializedName("parkId")
    String parkId;


    // * 发布时间
    @SerializedName("time")
    String time;

    //用户头像路径
    @SerializedName("userHeadPhoto")
    String userHeadPhoto;

    //用户名称
    @SerializedName("username")
    String username;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserHeadPhoto() {
        return prefix + userHeadPhoto;
    }

    public void setUserHeadPhoto(String userHeadPhoto) {
        this.userHeadPhoto = userHeadPhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //    final public String prefix = "http://10.34.7.132:8080/test";
    final public String prefix = "http://192.168.43.61:8080/test";
    //  final public String prefix = "http://120.79.18.242:8080/test/";
}
