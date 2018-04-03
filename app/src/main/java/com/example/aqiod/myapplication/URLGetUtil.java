package com.example.aqiod.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLGetUtil {
    public static String accessUrl(String getUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(getUrl);

            conn = (HttpURLConnection) url.openConnection();

            // 设置连接参数
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

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
