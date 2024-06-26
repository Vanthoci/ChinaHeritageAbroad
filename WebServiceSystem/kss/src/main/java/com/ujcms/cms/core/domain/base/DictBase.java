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
public class DictBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库表名
     */
    public static final String TABLE_NAME = "dict";

    @NotNull
    private Integer id = 0;

    /**
     * 数据字典类型ID
     */
    @NotNull
    @Schema(description="数据字典类型ID")
    private Integer typeId = 0;

    /**
     * 上级ID
     */
    @Nullable
    @Schema(description="上级ID")
    private Integer parentId;

    /**
     * 名称
     */
    @Length(max = 50)
    @NotNull
    @Schema(description="名称")
    private String name = "";

    /**
     * 值
     */
    @Length(max = 50)
    @NotNull
    @Schema(description="值")
    private String value = "";

    /**
     * 备注
     */
    @Length(max = 300)
    @Nullable
    @Schema(description="备注")
    private String remark;

    /**
     * 排列顺序
     */
    @NotNull
    @Schema(description="排列顺序")
    private Short order = 32767;

    @Nullable
    private Boolean sys = false;

    @Nullable
    private Boolean enabled = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Nullable
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Nullable
    public String getRemark() {
        return remark;
    }

    public void setRemark(@Nullable String remark) {
        this.remark = remark;
    }

    public Short getOrder() {
        return order;
    }

    public void setOrder(Short order) {
        this.order = order;
    }

    @Nullable
    public Boolean getSys() {
        return sys;
    }

    public void setSys(@Nullable Boolean sys) {
        this.sys = sys;
    }

    @Nullable
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(@Nullable Boolean enabled) {
        this.enabled = enabled;
    }
}