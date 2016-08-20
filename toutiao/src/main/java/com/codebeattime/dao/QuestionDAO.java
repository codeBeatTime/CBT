package com.codebeattime.dao;

import com.codebeattime.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = " q_describe, test_case, answer,name ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    //插入question
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,") values (#{qDescribe},#{testCase},#{answer},#{name})"})
     int addQuestion(Question question);

    //选择一定范围的题目
    @Select({"select",SELECT_FIELDS,"from" ,TABLE_NAME ,"order by id desc limit #{offset},#{limit}"})
    List<Question> getQuestionList(@Param("offset")int offset,@Param("limit") int limit);

    //根据id查询一个question
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Question getQuestionById(@Param("id")int id);
}
