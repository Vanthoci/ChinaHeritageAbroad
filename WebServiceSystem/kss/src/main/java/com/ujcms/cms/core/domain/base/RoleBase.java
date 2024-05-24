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
public class RoleBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库表名
     */
    public static final String TABLE_NAME = "role";

    @NotNull
    private Integer id = 0;

    /**
     * 站点ID
     */
    @Nullable
    @Schema(description="站点ID")
    private Integer siteId;

    @Length(max = 50)
    @NotNull
    private String name = "";

    /**
     * 描述
     */
    @Length(max = 300)
    @Nullable
    @Schema(description="描述")
    private String description;

    @Nullable
    private Boolean globalPermission = false;

    @Nullable
    private Boolean allArticlePermission = true;

    /**
     * 数据权限范围(1:所有,2:本组织,3:自身)
     */
    @NotNull
    @Schema(description="数据权限范围(1:所有,2:本组织,3:自身)")
    private Short dataScope = 1;

    /**
     * 等级
     */
    @NotNull
    @Schema(description="等级")
    private Short rank = 32767;

    /**
     * 类型(1:系统管理员,2:安全管理员,3:审计管理员,4:常规角色)
     */
    @NotNull
    @Schema(description="类型(1:系统管理员,2:安全管理员,3:审计管理员,4:常规角色)")
    private Short type = 4;

    /**
     * 共享范围(0:本站私有,1:子站点共享,2:全局共享)
     */
    @NotNull
    @Schema(description="共享范围(0:本站私有,1:子站点共享,2:全局共享)")
    private Short scope = 0;

    /**
     * 排序
     */
    @NotNull
    @Schema(description="排序")
    private Short order = 32767;

    @Nullable
    private Boolean allPermission = false;

    @Nullable
    private Boolean allGrantPermission = true;

    @Nullable
    private Boolean allChannelPermission = true;

    /**
     * 所有状态权限
     */
    @NotNull
    @Schema(description="所有状态权限")
    private Boolean allStatusPermission = false;

    /**
     * 功能权限
     */
    @Nullable
    @Schema(description="功能权限")
    private String permission;

    /**
     * 授权权限
     */
    @Nullable
    @Schema(description="授权权限")
    private String grantPermission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Nullable
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(@Nullable Integer siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public Boolean getGlobalPermission() {
        return globalPermission;
    }

    public void setGlobalPermission(@Nullable Boolean globalPermission) {
        this.globalPermission = globalPermission;
    }

    @Nullable
    public Boolean getAllArticlePermission() {
        return allArticlePermission;
    }

    public void setAllArticlePermission(@Nullable Boolean allArticlePermission) {
        this.allArticlePermission = allArticlePermission;
    }

    public Short getDataScope() {
        return dataScope;
    }

    public void setDataScope(Short dataScope) {
        this.dataScope = dataScope;
    }

    public Short getRank() {
        return rank;
    }

    public void setRank(Short rank) {
        this.rank = rank;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getScope() {
        return scope;
    }

    public void setScope(Short scope) {
        this.scope = scope;
    }

    public Short getOrder() {
        return order;
    }

    public void setOrder(Short order) {
        this.order = order;
    }

    @Nullable
    public Boolean getAllPermission() {
        return allPermission;
    }

    public void setAllPermission(@Nullable Boolean allPermission) {
        this.allPermission = allPermission;
    }

    @Nullable
    public Boolean getAllGrantPermission() {
        return allGrantPermission;
    }

    public void setAllGrantPermission(@Nullable Boolean allGrantPermission) {
        this.allGrantPermission = allGrantPermission;
    }

    @Nullable
    public Boolean getAllChannelPermission() {
        return allChannelPermission;
    }

    public void setAllChannelPermission(@Nullable Boolean allChannelPermission) {
        this.allChannelPermission = allChannelPermission;
    }

    public Boolean getAllStatusPermission() {
        return allStatusPermission;
    }

    public void setAllStatusPermission(Boolean allStatusPermission) {
        this.allStatusPermission = allStatusPermission;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    @Nullable
    public String getGrantPermission() {
        return grantPermission;
    }

    public void setGrantPermission(@Nullable String grantPermission) {
        this.grantPermission = grantPermission;
    }
}