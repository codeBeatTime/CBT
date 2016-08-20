package com.codebeattime.dao;

import com.codebeattime.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
@Mapper
public interface CommentDao {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "user_id, content, created_date, entity_id, entity_type,status";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{userId},#{content},#{createdDate}" +
            ",#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where entity_id=#{entityId} and entity_type=#{entityType}"})
    List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType")int entityType);

    @Select({"select count(id) from ",TABLE_NAME,"where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);
    @Update({"update",TABLE_NAME,"set status=#{status} where entity_id=#{entityId} and entity_type = #{entityType}"})
    public  void updateStatus(@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("status")int status);
}