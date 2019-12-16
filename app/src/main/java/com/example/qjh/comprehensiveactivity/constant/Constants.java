package com.example.qjh.comprehensiveactivity.constant;


/**
 * created:qjh
 * date:2019.11.22
 * function:后端接口
 */
public interface Constants {


    //final  String prefix="http://10.34.5.236:2080";
 //   final public String prefix = "http://120.79.18.242:8080/test";
    final public String prefix = "http://10.34.8.25:8080/test";

    final String test = prefix + "/user/test";
    final String Login = prefix + "/user/login";
    final String Register = prefix + "/user/register";
    //修改个人信息
    final String Modify = prefix + "/user/changeInformation";
    //创建车位
    final String CreatePark = prefix + "/park/createPark";
    //删除车位
    final String DeletePark = prefix + "/park/deletePark";
    //分享车位
    final String SharePark = prefix + "/park/cancelSharePark";
    //取消共享车位
    final String CancelSharePark = prefix + "/park/cancelSharePark";
    //查找自己的车位
    final String FindMyParkingLot = prefix + "/park/findMyPark";

    //删除自己的车位
    final String DeleteMyParkingLot = prefix + "/park/deletePark";
    //分享自己的车位
    final String ShareMyParkingLot = prefix + "/park/sharePark";
    //取消自己分享的车位
    final String CancelShareMyParkingLot = prefix + "/park/cancelSharePark";
    //修改密码
    final String ChangePwd = prefix + "/user/changePassword";
    //查找周围车位信息
    final String FindSharePark = prefix + "/park/findParkByDistance";




}
