package com.ujcms.cms.ext;

import com.ujcms.cms.core.service.SiteService;
import com.ujcms.cms.ext.service.*;
import com.ujcms.cms.ext.web.directive.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * ContextConfig
 *
 * @author PONY
 */
@Configuration
@MapperScan("com.ujcms.cms.ext.mapper")
public class ContextConfig implements InitializingBean {
    private final SiteService siteService;
    private final VoteService voteService;
    private final MessageBoardService messageBoardService;
    private final MessageBoardTypeService messageBoardTypeService;
    private final SurveyService surveyService;
    private final LeaderBoardService leaderBoardService;
    private final ExampleService exampleService;
    private final CommentsService commentsService;
    private final freemarker.template.Configuration configuration;

    public ContextConfig(SiteService siteService, VoteService voteService, MessageBoardService messageBoardService,
                         MessageBoardTypeService messageBoardTypeService, SurveyService surveyService,
                         LeaderBoardService leaderBoardService,
                         CommentsService commentsService,
                         ExampleService exampleService, freemarker.template.Configuration configuration) {
        this.siteService = siteService;
        this.voteService = voteService;
        this.messageBoardService = messageBoardService;
        this.messageBoardTypeService = messageBoardTypeService;
        this.surveyService = surveyService;
        this.leaderBoardService = leaderBoardService;
        this.exampleService = exampleService;
        this.commentsService = commentsService;
        this.configuration = configuration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configuration.setSharedVariable("Vote", new VoteDirective(voteService));
        configuration.setSharedVariable("VoteList", new VoteListDirective(voteService));
        configuration.setSharedVariable("VotePage", new VotePageDirective(voteService));
        configuration.setSharedVariable("MessageBoardList", new MessageBoardListDirective(messageBoardService));
        configuration.setSharedVariable("MessageBoardPage", new MessageBoardPageDirective(messageBoardService));
        configuration.setSharedVariable("MessageBoardTypeList",
                new MessageBoardTypeListDirective(messageBoardTypeService));
        configuration.setSharedVariable("Survey", new SurveyDirective(surveyService));
        configuration.setSharedVariable("SurveyList", new SurveyListDirective(surveyService));
        configuration.setSharedVariable("SurveyPage", new SurveyPageDirective(surveyService));
        configuration.setSharedVariable("LeaderBoardList",
                new LeaderBoardListDirective(leaderBoardService, siteService));
        configuration.setSharedVariable("LeaderBoardPage",
                new LeaderBoardPageDirective(leaderBoardService, siteService));
        configuration.setSharedVariable("Example", new ExampleDirective(exampleService));
        configuration.setSharedVariable("ExampleList", new ExampleListDirective(exampleService));
        configuration.setSharedVariable("ExamplePage", new ExamplePageDirective(exampleService));
        configuration.setSharedVariable("CommentsPage", new CommentsPageDirective(commentsService));
    }
}
