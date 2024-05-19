package com.ujcms.cms.ext.service.args;

import com.ujcms.commons.query.BaseQueryArgs;
import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.Nullable;

/**
 * Comments 查询参数
 *
 * @author Generator
 */
public class CommentsArgs extends BaseQueryArgs {
    private CommentsArgs(Map<String, Object> queryMap) {
        super(queryMap);
    }

    public CommentsArgs articleid(@Nullable Integer articleid) {
        // System.out.println(articleid);
        queryMap.put("EQ_articleId_Int", articleid);
        return this;
    }

    public CommentsArgs userid(@Nullable Integer userId) {
        System.out.println("uid: " + userId);
        queryMap.put("EQ_1_userId_Int", userId);
        queryMap.put("EQ_1_status_Short", 0);
        return this;
    }

    public static CommentsArgs of() {
        return of(new HashMap<>(16));
    }

    public static CommentsArgs of(Map<String, Object> queryMap) {
        return new CommentsArgs(queryMap);
    }
}