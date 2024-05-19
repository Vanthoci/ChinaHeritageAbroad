package com.ujcms.cms.core.mapper;

import com.ujcms.cms.ext.domain.Example;
import com.ujcms.commons.query.QueryInfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.ujcms.cms.core.domain.Article;
import com.ujcms.commons.db.order.OrderEntityMapper;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;

public interface BrowserMapper {

    /**
     * 根据查询条件获取列表
     *
     * @param query 查询条件
     * @return 数据列表
     */
    @Select("SELECT * FROM museum.ujcms_article_ext WHERE text_ LIKE CONCAT('%', #{query}, '%')")
    @Results({
            @Result(property = "title", column = "title"),
            @Result(property = "image", column = "image_"),
    })
    List<Article> selectArticles(@Nullable @Param("query") String cons);
}
