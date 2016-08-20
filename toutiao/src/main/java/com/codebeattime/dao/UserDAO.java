package com.codebeattime.dao;

import com.codebeattime.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by codebeattime on 2016/7/2.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, name, password, salt, head_url,ac_count";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ",SELECT_FIELDS, " from" ,TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Select({"select ",SELECT_FIELDS, " from" ,TABLE_NAME, " order by ac_count desc limit #{limit},#{offset}"})
    List<User> getUsersRank(@Param("limit")int limit,@Param("offset") int offset);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Update({"update ", TABLE_NAME, " set ac_count=#{acCount} where id=#{id}"})
    void updateAcCount(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

}
