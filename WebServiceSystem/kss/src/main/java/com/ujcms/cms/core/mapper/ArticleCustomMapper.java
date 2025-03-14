package com.ujcms.cms.core.mapper;

import com.ujcms.cms.core.domain.ArticleCustom;
import com.ujcms.commons.query.QueryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章自定义数据 Mapper
 *
 * @author PONY
 */
@Mapper
@Repository
public interface ArticleCustomMapper {
    /**
     * 插入数据
     *
     * @param bean 实体对象
     * @return 插入条数
     */
    int insert(ArticleCustom bean);

    /**
     * 根据查询条件获取列表
     *
     * @param queryInfo 查询条件
     * @return 数据列表
     */
    List<ArticleCustom> selectAll(@Nullable @Param("queryInfo") QueryInfo queryInfo);

    /**
     * 根据文章ID删除数据
     *
     * @param articleId 文章ID
     * @return 删除条数
     */
    int deleteByArticleId(Integer articleId);

    /**
     * 根据文章ID获取列表
     *
     * @param articleId 文章ID
     * @return 数据列表
     */
    List<ArticleCustom> listByArticleId(Integer articleId);

    /**
     * 根据栏目ID删除数据
     *
     * @param channelId 栏目ID
     * @return 被删除的数据条数
     */
    int deleteByChannelId(Integer channelId);

    /**
     * 根据站点ID删除数据
     *
     * @param siteId 站点ID
     * @return 被删除的数据条数
     */
    int deleteBySiteId(Integer siteId);
}