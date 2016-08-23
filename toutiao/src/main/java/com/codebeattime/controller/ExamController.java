package com.codebeattime.controller;

import com.codebeattime.dao.*;
import com.codebeattime.model.*;
import com.codebeattime.service.ExamService;
import com.codebeattime.service.QuestionService;
import com.codebeattime.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/21.
 */
@Controller
public class ExamController {

    @Autowired
    ExamQuestionDAO examQuestionDAO;
    @Autowired
    ExamDAO examDAO;
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    ExamService examService;
    @Autowired
    UserExamQuestionDAO userExamQuestionDAO;
    @Autowired
    UserDAO userDAO;
   @RequestMapping(path={"/exams/list"},method={RequestMethod.GET,RequestMethod.POST})
   public String getExamList(Model model){
       List<Exam> examList = examService.getExamList(0,100);
       model.addAttribute("examList",examList);
       return "exams";
   }
   //向某场考试中添加一道题
    @RequestMapping(path={"/exams/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addExamQuestion(HttpServletRequest request){
        String examId =  request.getParameter("examId");
        String questionId = request.getParameter("questionId");
        int eId = new Integer(examId);
        int qId = new Integer(questionId);
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.seteId(eId);
        examQuestion.setqId(qId);
        Map<String,Object> map =  examService.addExamQuestion(examQuestion);
        //插入没问题
        if(map.size()==0){
            return ToutiaoUtil.getJSONString(1);
        }
        return ToutiaoUtil.getJSONString(0,map);
    }
    //获取某场考试的详情页
    @RequestMapping(path = {"/exam/datil"},method ={RequestMethod.POST,RequestMethod.GET})
    public  String getExamDetail(@RequestParam("examId")int examId,Model model){
        List<Integer> questionsId = examQuestionDAO.getQuestionsByEId(examId);
        model.addAttribute("questionsId",questionsId);
        model.addAttribute("examId",examId);
        return "examDetail";
    }
    //获取某一场考试某一个题目信息并显示详情页
    @RequestMapping(path={"/examQuestion/datil"},method={RequestMethod.GET,RequestMethod.POST})
    public String getExamQuestionDetail(Model model,@RequestParam("questionId") int id,@RequestParam("examid")int examId){

        if(examId!=0){
            Exam exam = examDAO.getExamById(examId);
            model.addAttribute("exam",exam);
        }

        Question question = questionDAO.getQuestionById(id);

        model.addAttribute("question",question);

        return "examQuestionDetail";
    }
    //判一道考试题
    //接收用户答案 判断是否正确
    @RequestMapping(path={"exam/juage"},method = {RequestMethod.GET,RequestMethod.POST})

    public String juageExamAnswer(@RequestParam("questionId") int questionId, HttpServletRequest request,Model model,
    @RequestParam("examId")int examId) {
        Exam exam = examDAO.getExamById(examId);
        if(exam!=null){
            model.addAttribute("exam",exam);
        }
        String answer = request.getParameter("answer");
        //获取当前用户
        User user = hostHolder.getUser();
        if (user == null) return "home";
        //判断答案是否正确
        Question question = questionDAO.getQuestionById(questionId);
        Date date = new Date(System.currentTimeMillis());
        boolean isRight = examService.isRight(answer,question,user,exam,date);
        String result = "恭喜您结果正确";
        if (!isRight) result = "你的答案错误";
        model.addAttribute("result", result);
        model.addAttribute("question", question);

        return "examQuestionDetail";
    }
    //获取某一场考试的排名
    @RequestMapping(path={"/exam/rank"},method = {RequestMethod.GET})
    public String getExamRank(Model model ,@RequestParam("examId")int examId){
        List<UserAcCount> list = userExamQuestionDAO.getExamRank(examId);
        if(list.size()==0){
            System.out.println("考试号："+examId);
            System.out.println("listsize为0");
        }else{
            System.out.println("listsize为"+list.size());
            for(UserAcCount userAcCount:list){
                System.out.println(userAcCount.getuId()+"@@"+userAcCount.getCount());
            }
        }
        List<UserRank> listRank = new LinkedList<UserRank>();
        for(UserAcCount userAcCount:list){
            int uId = userAcCount.getuId();
            int count = userAcCount.getCount();
            UserRank userRank = new UserRank();
            userRank.setCount(count);
            userRank.setUser(userDAO.selectById(uId));
            listRank.add(userRank);
        }
        model.addAttribute("listRank",listRank);
        return "examRank";
    }
}
