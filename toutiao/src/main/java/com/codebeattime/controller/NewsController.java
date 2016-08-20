package com.codebeattime.controller;

import com.codebeattime.model.*;
import com.codebeattime.service.CommentService;
import com.codebeattime.service.NewsService;
import com.codebeattime.service.QiniuService;
import com.codebeattime.service.UserService;
import com.codebeattime.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Administrator on 2016/7/13.
 */
@Controller
public class NewsController {
    private  static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    //图片上传
    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public  String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"图片上传失败");
            }
            System.out.print("文件URL"+fileUrl);
            return  ToutiaoUtil.getJSONString(0,fileUrl);
        }catch(Exception e){
            logger.error("图片上传失败");
            return ToutiaoUtil.getJSONString(1,"图片上传失败");
        }
    }
    //图片下载
    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try{
            response.setContentType("image/jped");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误"+ e.getMessage());
        }
    }
    //增加一条新闻
    @RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public  String addNews(@RequestParam("image") String image,@RequestParam("title") String title,@RequestParam("link") String link){
        try {
            News news = new News();
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            if(hostHolder.getUser()==null){
                //匿名状态登陆默认3为匿名登录
                news.setUserId(3);
            }else {
                news.setUserId(hostHolder.getUser().getId());
            }
            //将新闻入库
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }catch(Exception e) {
            logger.error("增加新闻失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "增加新闻失败");
        }
    }
    //添加详情页
    @RequestMapping(path = {"/news/{newsId}"},method = RequestMethod.GET)

    public String newsDatil(@PathVariable("newsId") int newsId, Model model){
        //找到详情页的那条新闻
        News news = newsService.getById(newsId);
        if(news!=null){
            //获取详情页的所有评论
            List<Comment> comments = commentService.selectByEntity(news.getId(), EntityType.ENTITY_NEWS);
            //将评论显示
            List<ViewObject> vo = new ArrayList<ViewObject>() ;
            for(Comment comment :comments){
                ViewObject viewObject = new ViewObject();
                viewObject.set("comment",comment);
                viewObject.set("user",news.getUserId());
                vo.add(viewObject);
            }
            model.addAttribute("comments",vo);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        return "detail";
    }
    //增加一条评论
    @RequestMapping(path={"addComment"},method = {RequestMethod.POST})
     public String addComment(@RequestParam("newsId")int newsId,@RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setCreatedDate(new Date());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setStatus(0);
            comment.setUserId(hostHolder.getUser().getId());
            commentService.addComment(comment);
            //对新闻上信息的评论数量更新
            int count = commentService.getCommentCount(newsId,EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(newsId,count);

        }catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
            return "redirect:/news/" + String.valueOf(newsId);
    }
    //删除一条评论

}
