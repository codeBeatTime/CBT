package com.codebeattime.service;

import com.codebeattime.dao.QuestionDAO;
import com.codebeattime.dao.StaticImageDAO;
import com.codebeattime.model.Question;
import com.codebeattime.model.StaticImage;
import com.codebeattime.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class FileServer {
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private StaticImageDAO staticImageDAO;
    //选择一定范围的所有Questions
    public List<Question> getQuestionList(int offset,int limit){
        return questionDAO.getQuestionList(offset,limit);
    }

    public  String uploadFiles(MultipartFile[] questionFiles,String questionName){
        int N = questionFiles.length;
        if(N==0) return null;
        String[] result = new String[N];
        for(int i=0;i<questionFiles.length;i++){
           result[i] =  uploadFile(questionFiles[i]);
        }
        int numOfDate = 3;
        if(result.length!=numOfDate) return null;
        Question question = new Question();
        question.setqDescribe(result[0]);
        question.setTestCase(result[1]);
        question.setAnswer(result[2]);
        question.setName(questionName);
        question.setUrl(staticImageDAO.getRandImage().getUrl());
        questionDAO.addQuestion(question);
        return null;
    }
    public String uploadFile(MultipartFile file){

        if (!file.isEmpty()) {
            //过滤非txt文本的文件
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if(dotPos < 0) return null;
            String fileExt = file.getOriginalFilename().substring(dotPos+1);
            if(!ToutiaoUtil.isTXTFileAllowed(fileExt)) return null;

            //生成唯一的文件名
            String filename = UUID.randomUUID().toString().replaceAll("-","") + "."+fileExt;
            try {

                // 文件保存路径
                String filePath = ToutiaoUtil.FILE_DIR
                        + filename;
                // 转存文件
               // file.transferTo(new File(filePath));
                Files.copy(file.getInputStream(),new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

                //返回文件存储路径
                return filePath;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
