package com.ujcms.cms.core.domain.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

/**
 * This class was generated by MyBatis Generator.
 *
 * @author MyBatis Generator
 */
public class ChannelBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库表名
     */
    public static final String TABLE_NAME = "channel";

    @NotNull
    private Integer id = 0;

    /**
     * 站点ID
     */
    @NotNull
    @Schema(description="站点ID")
    private Integer siteId = 0;

    /**
     * 上级栏目ID
     */
    @Nullable
    @Schema(description="上级栏目ID")
    private Integer parentId;

    /**
     * 栏目模型ID
     */
    @NotNull
    @Schema(description="栏目模型ID")
    private Integer channelModelId = 0;

    /**
     * 文章模型ID
     */
    @NotNull
    @Schema(description="文章模型ID")
    private Integer articleModelId = 0;

    /**
     * 名称
     */
    @Length(max = 50)
    @NotNull
    @Schema(description="名称")
    private String name = "";

    /**
     * 别名
     */
    @Length(max = 50)
    @NotNull
    @Schema(description="别名")
    private String alias = "";

    @Nullable
    private Boolean nav = true;

    /**
     * 流程标识
     */
    @Length(max = 50)
    @Nullable
    @Schema(description="流程标识")
    private String processKey;

    /**
     * 类型(1:常规栏目,2:单页栏目,3:转向链接,4:链接到第一篇文章,5:链接到第一个子栏目)
     */
    @NotNull
    @Schema(description="类型(1:常规栏目,2:单页栏目,3:转向链接,4:链接到第一篇文章,5:链接到第一个子栏目)")
    private Short type = 1;

    /**
     * 层级
     */
    @NotNull
    @Schema(description="层级")
    private Short depth = 1;

    /**
     * 排列顺序
     */
    @NotNull
    @Schema(description="排列顺序")
    private Integer order = 999999;

    /**
     * 是否新窗口打开
     */
    @NotNull
    @Schema(description="是否新窗口打开")
    private Boolean targetBlank = false;

    /**
     * 图片
     */
    @Length(max = 255)
    @Nullable
    @Schema(description="图片")
    private String image;

    /**
     * 转向链接地址
     */
    @Length(max = 255)
    @Nullable
    @Schema(description="转向链接地址")
    private String linkUrl;

    /**
     * 文章模板
     */
    @Length(max = 255)
    @Nullable
    @Schema(description="文章模板")
    private String articleTemplate;

    /**
     * 栏目模板
     */
    @Length(max = 255)
    @Nullable
    @Schema(description="栏目模板")
    private String channelTemplate;

    /**
     * 绩效类型ID
     */
    @Nullable
    @Schema(description="绩效类型ID")
    private Integer performanceTypeId;

    /**
     * 每页条数
     */
    @NotNull
    @Schema(description="每页条数")
    private Short pageSize = 20;

    /**
     * 是否倒序排序
     */
    @NotNull
    @Schema(description="是否倒序排序")
    private Boolean orderDesc = true;

    /**
     * 是否允许评论
     */
    @NotNull
    @Schema(description="是否允许评论")
    private Boolean allowComment = true;

    /**
     * 是否允许投稿
     */
    @NotNull
    @Schema(description="是否允许投稿")
    private Boolean allowContribute = false;

    /**
     * 是否允许搜索
     */
    @NotNull
    @Schema(description="是否允许搜索")
    private Boolean allowSearch = true;

    /**
     * 创建日期
     */
    @NotNull
    @Schema(description="创建日期")
    private OffsetDateTime created = OffsetDateTime.now();

    /**
     * 修改日期
     */
    @NotNull
    @Schema(description="修改日期")
    private OffsetDateTime modified = OffsetDateTime.now();

    /**
     * 浏览次数
     */
    @NotNull
    @Schema(description="浏览次数")
    private Long views = 0L;

    /**
     * 栏目页浏览次数
     */
    @NotNull
    @Schema(description="栏目页浏览次数")
    private Long selfViews = 0L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @Nullable
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getChannelModelId() {
        return channelModelId;
    }

    public void setChannelModelId(Integer channelModelId) {
        this.channelModelId = channelModelId;
    }

    public Integer getArticleModelId() {
        return articleModelId;
    }

    public void setArticleModelId(Integer articleModelId) {
        this.articleModelId = articleModelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Nullable
    public Boolean getNav() {
        return nav;
    }

    public void setNav(@Nullable Boolean nav) {
        this.nav = nav;
    }

    @Nullable
    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(@Nullable String processKey) {
        this.processKey = processKey;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getDepth() {
        return depth;
    }

    public void setDepth(Short depth) {
        this.depth = depth;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getTargetBlank() {
        return targetBlank;
    }

    public void setTargetBlank(Boolean targetBlank) {
        this.targetBlank = targetBlank;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    @Nullable
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(@Nullable String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Nullable
    public String getArticleTemplate() {
        return articleTemplate;
    }

    public void setArticleTemplate(@Nullable String articleTemplate) {
        this.articleTemplate = articleTemplate;
    }

    @Nullable
    public String getChannelTemplate() {
        return channelTemplate;
    }

    public void setChannelTemplate(@Nullable String channelTemplate) {
        this.channelTemplate = channelTemplate;
    }

    @Nullable
    public Integer getPerformanceTypeId() {
        return performanceTypeId;
    }

    public void setPerformanceTypeId(@Nullable Integer performanceTypeId) {
        this.performanceTypeId = performanceTypeId;
    }

    public Short getPageSize() {
        return pageSize;
    }

    public void setPageSize(Short pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(Boolean orderDesc) {
        this.orderDesc = orderDesc;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public Boolean getAllowContribute() {
        return allowContribute;
    }

    public void setAllowContribute(Boolean allowContribute) {
        this.allowContribute = allowContribute;
    }

    public Boolean getAllowSearch() {
        return allowSearch;
    }

    public void setAllowSearch(Boolean allowSearch) {
        this.allowSearch = allowSearch;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getModified() {
        return modified;
    }

    public void setModified(OffsetDateTime modified) {
        this.modified = modified;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getSelfViews() {
        return selfViews;
    }

    public void setSelfViews(Long selfViews) {
        this.selfViews = selfViews;
    }
}