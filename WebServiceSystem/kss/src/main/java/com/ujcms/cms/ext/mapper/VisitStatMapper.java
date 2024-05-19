package com.ujcms.cms.ext.mapper;

import com.ujcms.cms.ext.domain.VisitStat;
import com.ujcms.commons.query.QueryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 访问统计 Mapper
 *
 * @author PONY
 */
@Mapper
@Repository
public interface VisitStatMapper {
    /**
     * 插入数据
     *
     * @param bean 实体对象
     * @return 插入条数
     */
    int insert(VisitStat bean);

    /**
     * 更新数据
     *
     * @param bean 实体对象
     * @return 更新条数
     */
    int update(VisitStat bean);

    /**
     * 删除数据
     *
     * @param id 主键ID
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键获取数据
     *
     * @param id 主键ID
     * @return 实体对象。没有找到数据，则返回 {@code null}
     */
    @Nullable
    VisitStat select(Integer id);

    /**
     * 根据查询条件获取列表
     *
     * @param queryInfo 查询条件
     * @return 数据列表
     */
    List<VisitStat> selectAll(@Nullable @Param("queryInfo") QueryInfo queryInfo);

    /**
     * 按日期统计
     *
     * @param siteId 站点ID
     * @param type   类型
     * @param begin  开始日期
     * @param end    介绍日期
     * @return 统计结果
     */
    List<VisitStat> statByDate(@Param("siteId") Integer siteId, @Param("type") Short type,
                               @Nullable @Param("begin") String begin, @Nullable @Param("end") String end);

    /**
     * 批量插入数据
     *
     * @param list 待插入数据
     * @return 插入数据量
     */
    int insertBatch(List<VisitStat> list);

    /**
     * 删除指定日期字符串之前的数据
     *
     * @param dateString 指定的日期字符串
     * @return 被删除的数据条数
     */
    int deleteBeforeDateString(@Param("dateString") String dateString);

    /**
     * 根据日期字符串删除数据
     *
     * @param dateString 日期字符串
     * @param type       统计类型
     * @return 被删除的数据条数
     */
    int deleteByDateString(@Param("dateString") String dateString, @Nullable @Param("type") Short type);

    /**
     * 根据站点ID删除数据
     *
     * @param siteId 站点ID
     * @return 被删除的数据条数
     */
    int deleteBySiteId(@Param("siteId") Integer siteId);
}