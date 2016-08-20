package com.codebeattime.service;

import com.alibaba.fastjson.JSONObject;
import com.codebeattime.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2016/7/14.
 */
@Service
public class QiniuService {
    private static  final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "AsFdFfLk-qgkY2hKQzD-KLLEBBaUEOZvpSBUv7Nv";
    String SECRET_KEY = "69jAJm-8HY7aU0zzB7abYH0jDdWqUCL9VZhAuav8";
    //要上传的空间
    String bucketname = "codebeattime";
    //上传到七牛后保存的文件名
    String key = "my-java.png";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        //判断文件类型是否合法
        String filexExt = file.getOriginalFilename().substring(dotPos + 1);
        if (!ToutiaoUtil.isFileAllowed(filexExt)) {
            return null;
        }
        //生成一个唯一的文件名
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + filexExt;

        try {
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), filename, getUpToken());
            if(res.isOK() && res.isJson()){
                String key = JSONObject.parseObject(res.bodyString()).get("key").toString();
                return ToutiaoUtil.QINIU_DOMAIN_PREFIX + key;
            }else {
                logger.error("七牛异常" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            logger.error("七牛上传错误" + e.getMessage());
            return null;
        }
    }
}
