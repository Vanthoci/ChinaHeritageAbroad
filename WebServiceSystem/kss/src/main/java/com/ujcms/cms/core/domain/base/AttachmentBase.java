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
public class AttachmentBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库表名
     */
    public static final String TABLE_NAME = "attachment";

    @NotNull
    private Integer id = 0;

    /**
     * 站点ID
     */
    @NotNull
    @Schema(description="站点ID")
    private Integer siteId = 0;

    /**
     * 用户ID
     */
    @NotNull
    @Schema(description="用户ID")
    private Integer userId = 0;

    /**
     * 文件名称
     */
    @Length(max = 150)
    @NotNull
    @Schema(description="文件名称")
    private String name = "";

    /**
     * 存储路径
     */
    @Length(max = 160)
    @NotNull
    @Schema(description="存储路径")
    private String path = "";

    /**
     * 访问路径
     */
    @Length(max = 160)
    @NotNull
    @Schema(description="访问路径")
    private String url = "";

    /**
     * 文件长度
     */
    @NotNull
    @Schema(description="文件长度")
    private Long length = 0L;

    /**
     * 创建时间
     */
    @NotNull
    @Schema(description="创建时间")
    private OffsetDateTime created = OffsetDateTime.now();

    @Nullable
    private Boolean used = false;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    @Nullable
    public Boolean getUsed() {
        return used;
    }

    public void setUsed(@Nullable Boolean used) {
        this.used = used;
    }
}