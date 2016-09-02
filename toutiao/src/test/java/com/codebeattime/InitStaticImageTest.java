package com.codebeattime;

import com.codebeattime.dao.*;
import com.codebeattime.model.*;
import com.codebeattime.util.ToutiaoUtil;
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
public class InitStaticImageTest {
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


        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = "AsFdFfLk-qgkY2hKQzD-KLLEBBaUEOZvpSBUv7Nv";
        String SECRET_KEY = "69jAJm-8HY7aU0zzB7abYH0jDdWqUCL9VZhAuav8";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth);

        //要列举文件的空间名
        String bucket = "staticimage";
        try {
            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            FileListing fileListing = bucketManager.listFiles(bucket,null,null,100,null);
            FileInfo[] items = fileListing.items;
            int index = 0;
            for(FileInfo fileInfo:items){
                System.out.print("第"+index+"图片");
                String str = ToutiaoUtil.QINIU_DOMAIN_PREFIX_STATICIMAGE  +fileInfo.key+ToutiaoUtil.IMAGE_FORMAT;
                staticImageDAO.addStaticImage(str);
                System.out.println(str);
                index++;
            }
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }

}
