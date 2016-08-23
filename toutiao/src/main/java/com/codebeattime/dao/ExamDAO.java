package com.codebeattime.dao;

import com.codebeattime.model.Exam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
@Mapper
public interface ExamDAO {
    String TABLE_NAME = "exam";
    String INSERT_FIELDS = " start_time,end_time,name ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    //获取考试列表
    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME ,"order by id desc limit #{offset},#{limit}"})
    List<Exam> getExamList(@Param("offset")int offset,@Param("limit")int limit);

    //增加一场考试
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{startTime},#{endTime},#{name})"})
    int addExam(Exam exam);
    //根据id查询一个考试
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id = #{id}"})
    Exam getExamById(int id);
}
