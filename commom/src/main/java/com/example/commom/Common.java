package com.example.commom;

/**
 * 通用的常量
 *
 * @author douliu
 * @date 2017/6/13
 */

public class Common {

    public interface Constants {

        //手机号校验的正则表达式
        String REGEX_MOBILE = "[1][3,4,5,6,7,8][0-9]{9}$";

        String API_URL = "http://116.196.112.35:8080/talker/api/";
//        String API_URL = "http://192.168.31.196:8080/api/";


    }
}
