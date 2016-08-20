package com.codebeattime.service;

import com.codebeattime.dao.NewsDAO;
import com.codebeattime.model.News;
import com.codebeattime.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by codebeattime on 2016/7/2.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }
    public String saveImage(MultipartFile file) throws IOException {
        //获取文件类型
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos<0){
            return null;
        }
        //判断文件类型是否合法
        String filexExt = file.getOriginalFilename().substring(dotPos+1);
        if(!ToutiaoUtil.isFileAllowed(filexExt)){
            return null;
        }
        //生成一个唯一的文件名
        String filename = UUID.randomUUID().toString().replaceAll("-","") + "." + filexExt;
        //文件存储
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + filename;
    }

    public  void addNews(News news){
        newsDAO.addNews(news);
    }
    public void updateCommentCount(int id,int commentCount ){
        newsDAO.updateCommentCount(id,commentCount);
    }
    public News getById(int newsId){
        return newsDAO.getById(newsId);
    }
}
