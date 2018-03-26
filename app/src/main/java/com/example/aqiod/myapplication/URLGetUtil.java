package com.example.aqiod.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class URLGetUtil {
    public static String accessUrl(String getUrl){
        URL url = null;
        try {
            url = new URL(getUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 设置连接参数
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        try {
            System.out.println(getUrl);
            if (conn.getResponseCode() == 200) {
                // 获取输入流
                InputStream is = conn.getInputStream();
                // 从输入流中读取服务器返回的数据
    //            String text = Tools.getTextFromStream(is);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
                return buffer.readLine();
            }
//            else if (conn.getResponseCode() == 500){
//                return "error";
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return "error";
    }
}
