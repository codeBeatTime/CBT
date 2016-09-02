package com.codebeattime;

import com.codebeattime.dao.*;
import com.codebeattime.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;

import java.util.Date;
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
    @Autowired
    private ExamDAO examDAO;
    @Autowired
    private ExamQuestionDAO examQuestionDAO;
    @Autowired
    private UserExamQuestionDAO userExamQuestionDAO;
    @Autowired
    private StaticImageDAO staticImageDAO;
    @Test
    public void initData() {
       for(int i=0;i<10;i++){
           StaticImage staticImage = staticImageDAO.getRandImage();
           System.out.println(staticImage.getUrl());
       }
    }

}
