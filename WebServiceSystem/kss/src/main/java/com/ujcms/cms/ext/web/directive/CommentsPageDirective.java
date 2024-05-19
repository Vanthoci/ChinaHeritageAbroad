package com.ujcms.cms.ext.web.directive;

import com.ujcms.cms.core.web.support.Directives;
import com.ujcms.cms.ext.domain.Comments;
import com.ujcms.cms.ext.service.CommentsService;
import com.ujcms.cms.ext.service.args.CommentsArgs;
import com.ujcms.commons.freemarker.Freemarkers;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.ujcms.commons.db.MyBatis.springPage;

/**
 * 示例分页 标签
 * <p>
 * Freemarker官方自定义标签文档：https://freemarker.apache.org/docs/pgui_datamodel_directive.html
 *
 * @author PONY
 */
public class CommentsPageDirective implements TemplateDirectiveModel {
    /**
     * 名称。字符串(String)。
     */
    public static final String ARTICLE = "articleid";
    public static final String USERID = "userid";
    public static final String SORTBY = "sortBy";


    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        // 检查返回参数是否存在
        Freemarkers.requireLoopVars(loopVars);
        // 检查标签体是否存在
        Freemarkers.requireBody(body);
        // 获取标签其它查询参数。非必须，可用 `CommentsArgs args = CommentsArgs.of();` 代替。
        CommentsArgs args = CommentsArgs.of(Directives.getQueryMap(params));
        // 获取标签的name参数
        Integer articleid = Directives.getInteger(params, ARTICLE);
        Integer userid = Directives.getInteger(params, USERID);
        // String sortBy = Directives.getString(params, SORTBY);
        // 加入name查询条件
        args.articleid(articleid);
        args.userid(userid);
        Directives.handleOrderBy(args.getQueryMap(), params, "created_desc");
        // args.sortBy(sortBy);
        // 第几页
        int page = Directives.getPage(params, env);
        // 每页条数
        int pageSize = Directives.getPageSize(params, env);
        // 查询数据
        Page<Comments> pagedList = springPage(service.selectPage(args, page, pageSize));
        // 将查询结果放入返回参数中
        loopVars[0] = env.getObjectWrapper().wrap(pagedList);
        // 执行标签体
        body.render(env.getOut());
    }

    private final CommentsService service;

    public CommentsPageDirective(CommentsService service) {
        this.service = service;
    }
}
