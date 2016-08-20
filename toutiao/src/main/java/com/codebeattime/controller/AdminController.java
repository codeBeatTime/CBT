package com.codebeattime.controller;

import com.codebeattime.service.FileServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/8/16.
 * 管理员控制程序
 */
@Controller
public class AdminController {
    private  static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private FileServer fileServer;

    //管理员入口
    @RequestMapping(path={"admin/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String  getAdminIndex(){
        return "adminHome";
    }

    @RequestMapping(path={"admin/uploadFile"},method = {RequestMethod.POST,RequestMethod.GET})
    //上传题目
    public String uploadFile(@RequestParam("questionFiles") MultipartFile[] questionFiles,@RequestParam("questionName") String questionName){
        System.out.println("三个文件上传成功了呢！！！！！！！！！！！！！！");
        System.out.print("输出当前路径");

       fileServer.uploadFiles(questionFiles,questionName);
        return "test";
    }

    //获取某一个文件
    //name是本地路径
    @RequestMapping(path={"/file"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void getFile(@RequestParam("name") String name, HttpServletResponse response){
        try{
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
            response.setContentType("text/plain");
            StreamUtils.copy(new FileInputStream(new File(name)),response.getOutputStream());
        }catch (Exception e){
            logger.error("文件下载错误"+e.getMessage());
        }
    }
}
