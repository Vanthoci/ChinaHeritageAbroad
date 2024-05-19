package com.ujcms.cms.ext.web.backendapi;

import static com.ujcms.cms.core.support.Constants.validPage;
import static com.ujcms.cms.core.support.Constants.validPageSize;
import static com.ujcms.cms.core.support.UrlConstants.BACKEND_API;
import static com.ujcms.commons.db.MyBatis.springPage;
import static com.ujcms.commons.query.QueryUtils.getQueryMap;

import com.ujcms.cms.core.aop.annotations.OperationLog;
import com.ujcms.cms.core.aop.enums.OperationType;
import com.ujcms.cms.core.support.Contexts;
import com.ujcms.cms.core.web.support.ValidUtils;
import com.ujcms.cms.ext.domain.Comments;
import com.ujcms.cms.ext.service.CommentsService;
import com.ujcms.cms.ext.service.args.CommentsArgs;
import com.ujcms.commons.web.Entities;
import com.ujcms.commons.web.Responses.Body;
import com.ujcms.commons.web.Responses;
import com.ujcms.commons.web.exception.Http400Exception;
import com.ujcms.commons.web.exception.Http404Exception;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("backendCommentsController")
@RequestMapping(BACKEND_API + "/ext/comments")
public class CommentsController {
    private final CommentsService service;

    private static final String NOT_FOUND = "Comments not found. ID = ";

    public CommentsController(CommentsService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('articleReview:list','*')")
    public Page<Comments> list(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, HttpServletRequest request) {
        CommentsArgs args = CommentsArgs.of(getQueryMap(request.getQueryString()));
        // System.out.println("called" + request.getQueryString());
        return springPage(service.selectPage(args, validPage(page), validPageSize(pageSize)));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('articleReview:show','*')")
    public Comments show(@PathVariable("id") int id) {
        return Optional.ofNullable(service.select(id))
        .orElseThrow(() -> new Http404Exception(NOT_FOUND + id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('articleReview:create','*')")
    @OperationLog(module = "comments", operation = "create", type = OperationType.CREATE)
    public ResponseEntity<Body> create(@RequestBody @Valid Comments bean) {
        Comments comments = new Comments();
        Entities.copy(bean, comments);
        service.insert(comments);
        return Responses.ok();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('articleReview:update','*')")
    @OperationLog(module = "comments", operation = "update", type = OperationType.UPDATE)
    public ResponseEntity<Body> update(@RequestBody @Valid Comments bean) {
        Comments comments = Optional.ofNullable(service.select(bean.getId()))
        .orElseThrow(() -> new Http400Exception(NOT_FOUND + bean.getId()));
        Entities.copy(bean, comments);
        service.update(comments);
        return Responses.ok();
    }

    @PutMapping("status")
    @PreAuthorize("hasAnyAuthority('articleReview:updateStatus','*')")
    @OperationLog(module = "messageBoard", operation = "updateStatus", type = OperationType.UPDATE)
    public ResponseEntity<Body> updateStatus(@RequestBody @Valid UpdateStatusParams params) {
        params.ids.stream().filter(Objects::nonNull).map(service::select)
                .filter(Objects::nonNull).forEach(bean -> {
            bean.setStatus(params.status);
            service.update(bean);
        });
        return Responses.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('articleReview:delete','*')")
    @OperationLog(module = "comments", operation = "delete", type = OperationType.DELETE)
    public ResponseEntity<Body> delete(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            Comments bean = Optional.ofNullable(service.select(id))
            .orElseThrow(() -> new Http400Exception(NOT_FOUND + id));
            service.delete(bean.getId());
        }
        return Responses.ok();
    }


    public static class UpdateStatusParams implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotNull
        private List<Integer> ids;
        @NotNull
        @Min(0)
        @Max(2)
        private Short status;

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }

        public Short getStatus() {
            return status;
        }

        public void setStatus(Short status) {
            this.status = status;
        }
    }
}