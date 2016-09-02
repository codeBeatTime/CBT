package com.codebeattime.service;

import com.codebeattime.dao.ExamDAO;
import com.codebeattime.dao.ExamQuestionDAO;
import com.codebeattime.dao.QuestionDAO;
import com.codebeattime.dao.UserExamQuestionDAO;
import com.codebeattime.model.*;
import com.codebeattime.util.ToutiaoUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/21.
 */
@Service
public class ExamService {
    @Autowired
    private ExamDAO examDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private ExamQuestionDAO examQuestionDAO;
    @Autowired
    private UserExamQuestionDAO userExamQuestionDAO;
    public List<Exam> getExamList(int offset,int limit){
        return examDAO.getExamList(offset,limit);
    }
    //向某场考试中插入一道题
    public Map<String,Object> addExamQuestion(ExamQuestion examQuestion){
        Map<String,Object> map = new HashMap<String,Object>();
        Question question= questionDAO.getQuestionById(examQuestion.getqId());
        if(question==null) {
            map.put("questionId","题目id不存在");
        }
        Exam exam = examDAO.getExamById(examQuestion.geteId());
        if(exam==null) map.put("examId","考试id不存在");
        int line = examQuestionDAO.addExamQuestion(examQuestion);
        if(line==-1) map.put("insertExamQuestion","插入examQuestion失败");
        return map;
    }
    //判断考试题是否正确
    public boolean isRight(String answer,Question question,User user,Exam exam,Date date){

        //判断题目是否正确
        String rightAnswer = "";
        try {
            rightAnswer = ToutiaoUtil.readFromFile(question.getAnswer());

        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean result = rightAnswer.equals(answer);
        if(result){
            //题目正确的操作
            rightAction(user,exam,question,date);
        }

        return result;
    }
    //判断考试题是否已经被正确提交了
    public boolean hasAc(int userId,int examId,int questionId){
        UserExamQuestion userExamQuestion = userExamQuestionDAO.getByUEQID(userId,examId,questionId);
        if(userExamQuestion==null){
            return false;
        }
        return true;
    }
    //题目正确的操作
    public void rightAction(User user,Exam exam,Question question,Date date){
        //过滤过期情况
        if(date.compareTo(exam.getStartTime())==-1 || date.compareTo(exam.getEndTime())==1){
            return;
        }
        //插入考试通过记录
        UserExamQuestion userExamQuestion = new UserExamQuestion();
        userExamQuestion.setuId(user.getId());
        userExamQuestion.seteId(exam.getId());
        userExamQuestion.setqId(question.getId());
        userExamQuestionDAO.addUserExamQuestion(userExamQuestion);
    }

}
