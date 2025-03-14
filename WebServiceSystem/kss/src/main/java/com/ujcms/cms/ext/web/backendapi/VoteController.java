package com.ujcms.cms.ext.web.backendapi;

import static com.ujcms.cms.core.support.Constants.validPage;
import static com.ujcms.cms.core.support.Constants.validPageSize;
import static com.ujcms.cms.core.support.UrlConstants.BACKEND_API;
import static com.ujcms.commons.db.MyBatis.springPage;
import static com.ujcms.commons.query.QueryUtils.getQueryMap;

import com.fasterxml.jackson.annotation.JsonView;
import com.ujcms.cms.core.aop.annotations.OperationLog;
import com.ujcms.cms.core.aop.enums.OperationType;
import com.ujcms.cms.core.support.Contexts;
import com.ujcms.cms.core.web.support.ValidUtils;
import com.ujcms.cms.ext.domain.Vote;
import com.ujcms.cms.ext.service.VoteService;
import com.ujcms.cms.ext.service.args.VoteArgs;
import com.ujcms.commons.web.Entities;
import com.ujcms.commons.web.Responses.Body;
import com.ujcms.commons.web.Responses;
import com.ujcms.commons.web.Views;
import com.ujcms.commons.web.exception.Http404Exception;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 投票 Controller
 *
 * @author PONY
 */
@RestController("backendVoteController")
@RequestMapping(BACKEND_API + "/ext/vote")
public class VoteController {
    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @GetMapping
    @JsonView(Views.List.class)
    @PreAuthorize("hasAnyAuthority('vote:list','*')")
    public Page<Vote> list(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize,
                           HttpServletRequest request) {
        VoteArgs args = VoteArgs.of(getQueryMap(request.getQueryString()));
        args.siteId(Contexts.getCurrentSiteId());
        return springPage(service.selectPage(args, validPage(page), validPageSize(pageSize)));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('vote:show','*')")
    public Vote show(@PathVariable("id") int id) {
        Vote bean = service.select(id);
        if (bean == null) {
            throw new Http404Exception("Vote not found. ID = " + id);
        }
        return bean;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('vote:create','*')")
    @OperationLog(module = "vote", operation = "create", type = OperationType.CREATE)
    public ResponseEntity<Body> create(@RequestBody @Valid Vote bean) {
        Vote vote = new Vote();
        Entities.copy(bean, vote, "siteId", "total");
        vote.setSiteId(Contexts.getCurrentSiteId());
        service.insert(vote, bean.getOptions());
        return Responses.ok();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('vote:update','*')")
    @OperationLog(module = "vote", operation = "update", type = OperationType.UPDATE)
    public ResponseEntity<Body> update(@RequestBody @Valid Vote bean) {
        Vote vote = service.select(bean.getId());
        if (vote == null) {
            throw new Http404Exception("Vote not found. ID = " + bean.getId());
        }
        ValidUtils.dataInSite(bean.getSiteId(), Contexts.getCurrentSiteId());
        Entities.copy(bean, vote, "siteId", "total");
        service.update(vote, bean.getOptions());
        return Responses.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('vote:delete','*')")
    @OperationLog(module = "vote", operation = "delete", type = OperationType.DELETE)
    public ResponseEntity<Body> delete(@RequestBody List<Integer> ids) {
        Integer siteId = Contexts.getCurrentSiteId();
        for (Integer id : ids) {
            Vote bean = service.select(id);
            if (bean == null) {
                continue;
            }
            ValidUtils.dataInSite(bean.getSiteId(), siteId);
            service.delete(bean.getId());
        }
        service.delete(ids);
        return Responses.ok();
    }
}