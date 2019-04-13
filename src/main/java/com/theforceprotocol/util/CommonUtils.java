package com.theforceprotocol.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class CommonUtils {
    public static String littleEndian2BigEndianString(String original) {
        String reverse = "";
        int length = original.length();
        for(int i = length - 1; i >=0;i -=2)
            reverse =reverse +original.substring(i -1,i +1);
        return reverse;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 计算手续费
     *
     * @param inputCount
     * @return
     */
    public static BigDecimal calculationFee(int inputCount) {
        //计算手续费获取每个字节的手续费
        /*
        {"fastestFee":30,"halfHourFee":30,"hourFee":28}
         */
        String url="https://bitcoinfees.earn.com/api/v1/fees/recommended";
        //计算字节大小和费用
        String result=CommonUtils.sendGet(url,null);
        JSONObject response = JSON.parseObject(result);
        BigDecimal keyCount = BigDecimal.valueOf((inputCount * 148 + 44) * ((int)response.get("hourFee")/4));
        return keyCount;
    }

}