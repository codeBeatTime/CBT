package com.codebeattime.dao;

import com.codebeattime.model.StaticImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2016/8/24.
 */
@Mapper
public interface StaticImageDAO {
    String TABLE_NAME = "static_image";
    String INSET_FIELDS = " url ";
    String SELECT_FIELDS = "id," + INSET_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSET_FIELDS,") values (#{url})"})
    int addStaticImage(String url);

    //随机得到一个图片
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"order by rand() limit 0,1"})
    StaticImage getRandImage();
}
