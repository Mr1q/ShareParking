package com.example.qjh.comprehensiveactivity.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("addTime")
    private String myaddtime;

    @SerializedName("username")
    private String myusername;

    @SerializedName("address")
    private String myaddress;


    @SerializedName("id")
    private String myid;

    @SerializedName("name")
    private String myname;

    @SerializedName("password")
    private String mypasswprd;

    @SerializedName("phone")
    private String myphone;

    @SerializedName("headPhotoURL")
    private String headPhotoURL;

    @SerializedName("car_number")
    private String car_number;

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getHeadPhotoURL() {
        return prefix + headPhotoURL;
    }
    public String getPhotoURL() {
        return  headPhotoURL;
    }

   // final public String prefix = "http://120.79.18.242:8080/test";
   final public String prefix = "http://192.168.43.61:8080/test";
    public void setHeadPhotoURL(String headPhotoURL) {
        this.headPhotoURL = headPhotoURL;
    }

    public String getMyaddtime() {
        return myaddtime;
    }

    public void setMyaddtime(String myaddtime) {
        this.myaddtime = myaddtime;
    }

    public String getMyusername() {
        return myusername;
    }

    public void setMyusername(String myusername) {
        this.myusername = myusername;
    }

    public String getMyaddress() {
        return myaddress;
    }

    public void setMyaddress(String myaddress) {
        this.myaddress = myaddress;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getMypasswprd() {
        return mypasswprd;
    }

    public void setMypasswprd(String mypasswprd) {
        this.mypasswprd = mypasswprd;
    }

    public String getMyphone() {
        return myphone;
    }

    public void setMyphone(String myphone) {
        this.myphone = myphone;
    }


}
