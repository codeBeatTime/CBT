package com.codebeattime.controller;

import com.codebeattime.dao.QuestionDAO;
import com.codebeattime.dao.UserDAO;
import com.codebeattime.model.HostHolder;
import com.codebeattime.model.Question;
import com.codebeattime.model.User;
import com.codebeattime.service.FileServer;
import com.codebeattime.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
@Controller
public class QuestionController {
    private  static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private FileServer fileServer;
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserDAO userDAO;
    //获取题目List
    @RequestMapping(path={"/questions/list"},method = {RequestMethod.GET,RequestMethod.POST})
    public String getQuestionsList(Model model){
        //获取所有Questions 复制给model
        List<Question> questions = fileServer.getQuestionList(0,50);
        model.addAttribute("questions",questions);
        return "questions";
    }
    //获取某一个题目信息并显示详情页
    @RequestMapping(path={"/questions/datil"},method={RequestMethod.GET,RequestMethod.POST})
    public String getQuestionDetail(Model model,@RequestParam("questionId") int id){
        Question question = questionDAO.getQuestionById(id);
        model.addAttribute("question",question);
        return "questionDetail";
    }
    //接收用户答案 判断是否正确
    @RequestMapping(path={"questions/juage"},method = {RequestMethod.GET,RequestMethod.POST})

    public String juageAnswer(@RequestParam("questionId") int questionId, HttpServletRequest request,Model model){

        String answer =  request.getParameter("answer");
        //获取当前用户
        User user = hostHolder.getUser();
        if(user==null) return "home";
        //判断答案是否正确
        Question question = questionDAO.getQuestionById(questionId);
        boolean isRight = questionService.isRight(answer,question,user);
        String result = "恭喜您结果正确";
        if(!isRight) result = "你的答案错误";
        model.addAttribute("result",result);
        model.addAttribute("question",question);
        return "questionDetail";
    }
    //返回用户排名表单
    @RequestMapping(path={"/questions/rank"},method = {RequestMethod.GET,RequestMethod.POST})
    public String rankByAcCount(@RequestParam("limit")int limit,@RequestParam("offset") int offset,Model model){
        List<User> userList = userDAO.getUsersRank(limit,offset);
        model.addAttribute("userList",userList);
        return  "questionRank";
    }
}
