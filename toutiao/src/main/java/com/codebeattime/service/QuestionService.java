package com.codebeattime.service;

import com.codebeattime.dao.UserDAO;
import com.codebeattime.dao.UserExamQuestionDAO;
import com.codebeattime.dao.UserQuestionDAO;
import com.codebeattime.model.Question;
import com.codebeattime.model.User;
import com.codebeattime.model.UserQuestion;
import com.codebeattime.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Administrator on 2016/8/18.
 */
@Service
public class QuestionService {
    @Autowired
    private UserQuestionDAO userQuestionDAO;
    @Autowired
    private UserDAO userDAO;

    //判断题目和答案是否相等
    public boolean isRight(String answer,Question question,User user){

        String rightAnswer = "";
        try {
             rightAnswer = ToutiaoUtil.readFromFile(question.getAnswer());

        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean result = rightAnswer.equals(answer);
        //如果已经做过了则什么都不做
        //如果没有做过，count++，uq表中增加该项
        if(isAlreadyAc(question,user)){

        }else{
            addCount(question,user);
        }
        return  result;
    }
    //查询该题目，该用户是否之前通过了
    public boolean isAlreadyAc(Question question,User user){
        boolean result = false;
        UserQuestion userQuestion = userQuestionDAO.selectByUserAndQuestion(user.getId(),question.getId());
        if(userQuestion!=null) result = true;
        return result;
    }
    //通过一道题的处理
    private void addCount(Question question,User user){
        //首先加到用户-题目列表中
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setuId(user.getId());
        userQuestion.setqId(question.getId());
        userQuestionDAO.addUserQuestion(userQuestion);
        //对该用户AC总数+1
        user.setAcCount(user.getAcCount()+1);
        userDAO.updateAcCount(user);
    }
}
