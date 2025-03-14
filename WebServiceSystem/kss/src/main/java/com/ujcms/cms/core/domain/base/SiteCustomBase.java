package com.ujcms.cms.core.domain.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

/**
 * This class was generated by MyBatis Generator.
 *
 * @author MyBatis Generator
 */
public class SiteCustomBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库表名
     */
    public static final String TABLE_NAME = "site_custom";

    @NotNull
    private Long id = 0L;

    /**
     * 站点ID
     */
    @NotNull
    @Schema(description="站点ID")
    private Integer siteId = 0;

    /**
     * 名称
     */
    @Length(max = 50)
    @NotNull
    @Schema(description="名称")
    private String name = "";

    /**
     * 类型
     */
    @Length(max = 32)
    @NotNull
    @Schema(description="类型")
    private String type = "text";

    /**
     * 值
     */
    @Nullable
    @Schema(description="值")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public void setValue(@Nullable String value) {
        this.value = value;
    }
}