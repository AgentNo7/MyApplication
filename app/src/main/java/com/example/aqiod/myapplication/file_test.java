package com.example.aqiod.myapplication;

import java.io.*;

public class file_test {
    public static void main(String[] args) {
        String filePath = "E:/big.jpg";
        File file = new File(filePath);
//        String url = "http://localhost:8080/file/upload.do";
//        String res = UploadUtil.uploadFile(file, url);
//        System.out.println(res);
//        ResponseObject responseObject = Utils.parseJson(res);
//        System.out.println("status :  " + responseObject.getCode() + "url: " + responseObject.getUrl());

        System.out.println(URLGetUtil.accessUrl("http://addfile.sevendegree.date:8080/user/login.do?username=test2"));

        String result = UploadUtil.uploadFile(file, "http://addfile.sevendegree.date:8080/file/upload.do");
        System.out.println(result);
    }
}
