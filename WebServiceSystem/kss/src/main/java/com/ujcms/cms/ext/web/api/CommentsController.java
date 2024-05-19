package com.ujcms.cms.ext.web.api;

import com.ujcms.cms.ext.web.api.EmotionHelper;
import com.fasterxml.jackson.annotation.JsonView;
import com.ujcms.cms.core.web.support.Directives;
import com.ujcms.cms.ext.domain.Comments;
import com.ujcms.cms.ext.service.CommentsService;
import com.ujcms.cms.ext.service.args.CommentsArgs;
import com.ujcms.commons.captcha.CaptchaTokenService;
import com.ujcms.commons.query.QueryUtils;
import com.ujcms.commons.web.Entities;
import com.ujcms.commons.web.Responses;
import com.ujcms.commons.web.Servlets;
import com.ujcms.commons.web.Views;
import com.ujcms.commons.web.exception.Http400Exception;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.ujcms.cms.core.support.UrlConstants.API;
import static com.ujcms.cms.core.support.UrlConstants.FRONTEND_API;
import static com.ujcms.commons.db.MyBatis.springPage;
import static com.ujcms.commons.query.QueryUtils.QUERY_PREFIX;

/**
 * 示例 API Controller
 *
 * @author PONY
 */
@Tag(name = "CommentsController", description = "评论接口")
@RestController
@RequestMapping({API + "/comments", FRONTEND_API + "/comments"})
public class CommentsController {
    private final CommentsService service;

    public CommentsController(CaptchaTokenService captchaTokenService, CommentsService service) {
        this.service = service;
    }


    @Operation(summary = "提交示例")
    @PostMapping
    public ResponseEntity<Responses.Body> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "评论对象")
            @RequestBody @Valid CommentsParams params,
            HttpServletRequest request) {

        String ip = Servlets.getRemoteAddr(request);
        String text = params.getText();
        if (text == null || text.isEmpty()) {
            return Responses.failure("不能提交空评论！");
        }

        Comments comments = new Comments();
        Entities.copy(params, comments);
        comments.setStatus((short)1);
        comments.setIp(ip);
        try {
            comments.setPositiveLevel(EmotionHelper.getPositiveLevel(text));
        } catch (IOException e) {
            comments.setPositiveLevel(0.5);
        }
        service.insert(comments);
        return Responses.ok();
    }

    @Schema(name = "CommentsController.CommentsParams", description = "示例参数")
    public static class CommentsParams extends Comments {
        private static final long serialVersionUID = 1L;
    }
}
