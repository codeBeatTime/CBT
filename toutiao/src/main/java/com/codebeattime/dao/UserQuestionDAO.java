package com.codebeattime.dao;

import com.codebeattime.model.UserQuestion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2016/8/19.
 */
@Mapper
public interface UserQuestionDAO {
    String TABLE_NAME = "user_question";
    String INSET_FIELDS = "u_id,q_id";
    String SELECT_FIELDS = "id," + INSET_FIELDS;
    //根据用户id和题目id查询是否存在
     @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where u_id =#{uId} AND q_id = #{qId}"})
     UserQuestion selectByUserAndQuestion(@Param("uId")int uId,@Param("qId") int qId);

    //根据用户id和题目id插入
    @Insert({"insert into",TABLE_NAME,"(",INSET_FIELDS,") values (#{uId},#{qId})"})
    int addUserQuestion(UserQuestion userQuestion);
}
