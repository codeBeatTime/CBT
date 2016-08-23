package com.codebeattime.dao;

import com.codebeattime.model.ExamQuestion;
import com.codebeattime.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
@Mapper
public interface ExamQuestionDAO {
    String TABLE_NAME = "exam_question";
    String INSERT_FIELDS = " e_id,q_id";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    //插入一条考试题
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{eId},#{qId})"})
    int addExamQuestion(ExamQuestion examQuestion);

    //选择某场考试的所有题目ID
    @Select({"select ","q_id","from",TABLE_NAME,"where e_id = #{eId}"})
    List<Integer> getQuestionsByEId(@Param("eId")int eId);
}
