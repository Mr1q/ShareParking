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
