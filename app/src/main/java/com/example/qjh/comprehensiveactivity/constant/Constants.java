package com.example.qjh.comprehensiveactivity.constant;


/**
 * created:qjh
 * date:2019.11.22
 * function:后端接口
 */
public interface Constants {


  //final  String prefix="http://10.34.5.236:2080";
    final  String prefix="http://120.79.18.242:2080";
    final  String test=prefix+"/user/test";
    final  String Login=prefix+"/user/login";
    final  String Register=prefix+"/user/register";

    //修改个人信息
    final  String Modify=prefix+"/user/changeInformation";
    //创建车位
    final  String CreatePark=prefix+"/park/createPark";


}
