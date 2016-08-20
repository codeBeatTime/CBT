package com.codebeattime.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;


/**
 * Created by codebeattime on 2016/7/3.
 */
public class ToutiaoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);
    public  static String TOUTIAO_DOMAIN = "http://codebeattime.online/";
    public  static String QINIU_DOMAIN_PREFIX = "http://oaaktbc5o.bkt.clouddn.com/";
    public static String IMAGE_DIR = "E:/upload/";
    public static String FILE_DIR = "E:/uploadFiles/";
    public static String[] IMAGE_FILE_EXTD = {"png","bmp","jpg","jpeg"};
    public static String[] TXT_FILE_EXID = {"txt"};
    //读取文件的内容并以字符串返回
    //返回字符串的长度可能长于正常的结果
    public static String readFromFile(String name) throws IOException {
        StringBuilder result = new StringBuilder();
        File file = new File(name);
        FileReader fileReader = new FileReader(file);
        BufferedReader br= new BufferedReader(fileReader);
        String str = br.readLine();

        while(str!=null){
            result.append(str+"\r\n");
           str = br.readLine();

        }
        return result.toString();
    }
    //返回jsonstring
    public static String getJSONString(int code){
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }
    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    //按照文件后缀名判断是否是图片
    public  static boolean isFileAllowed(String filename){
        for(String name :IMAGE_FILE_EXTD){
            if(name.equals(filename)){
                return true;
            }
        }
        return false;
    }
    //按照文件后缀名判断是否是txt文件
    public static boolean isTXTFileAllowed(String filename){
        for(String name: TXT_FILE_EXID){
            if(name.equals(filename)) return true;
        }
        return false;
    }

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}
