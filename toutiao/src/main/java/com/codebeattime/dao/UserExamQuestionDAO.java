package com.codebeattime.dao;

import com.codebeattime.model.UserAcCount;
import com.codebeattime.model.UserExamQuestion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
@Mapper
public interface UserExamQuestionDAO {
    String TABLE_NAME = "user_exam_question";
    String INSET_FIELDS = "u_id,e_id,q_id";
    String SELECT_FIELDS = "id," + INSET_FIELDS;
    //根据用户id、考试、题目id查询
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where u_id=#{uId} AND e_id=#{eId} AND q_id=#{qId}"})
    UserExamQuestion getByUEQID(@Param("uId")int uId,@Param("eId")int eId,@Param("qId")int qId);

    @Insert({"insert into",TABLE_NAME,"(",INSET_FIELDS,") values(#{uId},#{eId},#{qId})"})
    int addUserExamQuestion(UserExamQuestion userExamQuestion);

    //获取某场考试的用户排名
    //@Select({"select ","u_id,count(q_id) from ",TABLE_NAME,"where e_id=#{examId} group by u_id order by count(q_id) desc"})
    List<UserAcCount> getExamRank(int examId);
}
