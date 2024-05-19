package com.ujcms.cms.ext.domain;

import com.ujcms.cms.ext.domain.base.CommentsBase;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

public class Comments extends CommentsBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @Length(max = 30)
    @NotNull
    @Schema(description="用户名")
    private String username = "";

    /**
     * 头像URL
     */
    @Length(max = 255)
    @Nullable
    @Schema(description="头像URL")
    private String avatar;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }
}