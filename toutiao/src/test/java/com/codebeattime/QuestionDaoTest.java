package com.codebeattime;

import com.codebeattime.dao.QuestionDAO;
import com.codebeattime.dao.UserQuestionDAO;
import com.codebeattime.model.Question;
import com.codebeattime.model.UserQuestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class QuestionDaoTest {
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private UserQuestionDAO userQuestionDAO;
    @Test
    public void initData() {
        for (int i=0;i<10;i++){
            Question question = new Question();
            question.setAnswer("3");
            question.setqDescribe(""+i);
            question.setTestCase("3");
            question.setName("question"+i);
            questionDAO.addQuestion(question);
            UserQuestion userQuestion = new UserQuestion();
            userQuestion.setuId(i);
            userQuestion.setqId(i+1);
            userQuestionDAO.addUserQuestion(userQuestion);
        }
        List<Question>  questions = questionDAO.getQuestionList(0,3);
        UserQuestion userQuestion = userQuestionDAO.selectByUserAndQuestion(1,2);
    }
}
