package com.codebeattime.dao;

import com.codebeattime.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by codebeattime on 2016/7/2.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"Select ", SELECT_FIELDS,"from",TABLE_NAME,"where id = #{newsId}"})
    News getById(int newsId);
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);

    @Update({"update",  TABLE_NAME,  "set comment_count=#{commentCount} where id = #{id}"})
    void updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);
}
