package com.example.aqiod.myapplication;


import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String codes = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final int codeLen = codes.length();

    public static String IdentifyCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            stringBuilder.append(codes.charAt(new Random().nextInt(codeLen)));
        }
        return stringBuilder.toString();
    }

    public static ResponseObject parseJson(String json) {
        ResponseObject responseObject = null;
        String pattern = "(\"status\":)(\\d)(.*)(\"url\":\")(.*)(\")";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(json);
        if (m.find()) {
            responseObject = new ResponseObject();
            int code = Integer.parseInt(m.group(2));
            responseObject.setCode(code);
            String url = m.group(5);
            responseObject.setUrl(url);
        }
        return responseObject;
    }

    public static int getStatus(String json) {
        int code = -1;
        String pattern = "(\"status\":)(\\d)(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(json);
        if (m.find()) {
            code = Integer.parseInt(m.group(2));
        }
        return code;
    }

    public static void main(String[] args) {
        parseJson("{\"status\":0,\"data\":{\"uri\":\"big.jpg\",\"url\":\"http://adsf.sevendegree.date/big.jpg\"}}");
    }
}
