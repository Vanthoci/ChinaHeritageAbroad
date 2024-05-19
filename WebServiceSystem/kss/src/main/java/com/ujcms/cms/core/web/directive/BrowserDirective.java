package com.ujcms.cms.core.web.directive;

import com.ujcms.cms.core.domain.Article;
import com.ujcms.cms.core.service.ArticleService;
import com.ujcms.cms.core.web.support.Directives;
import com.ujcms.cms.ext.domain.Example;
import com.ujcms.cms.ext.service.ExampleService;
import com.ujcms.commons.freemarker.Freemarkers;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;
import java.util.*;

public class BrowserDirective implements TemplateDirectiveModel {
    private final ArticleService articleService;

    public BrowserDirective(ArticleService articleService) {
        this.articleService = articleService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {

        Freemarkers.requireLoopVars(loopVars);
        Freemarkers.requireBody(body);

        String cons = Directives.getString(params, "q");

        List<Article> bean = articleService.selectArticles(cons);
        System.out.println(bean);

        loopVars[0] = env.getObjectWrapper().wrap(bean);

        body.render(env.getOut());
    }
}
