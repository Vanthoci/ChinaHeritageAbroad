package com.ujcms.cms.ext.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.ujcms.cms.core.service.SeqService;
import com.ujcms.cms.ext.domain.Comments;
import com.ujcms.cms.ext.domain.base.CommentsBase;
import com.ujcms.cms.ext.mapper.CommentsMapper;
import com.ujcms.cms.ext.service.args.CommentsArgs;
import com.ujcms.commons.query.QueryInfo;
import com.ujcms.commons.query.QueryParser;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentsService {
    private final CommentsMapper mapper;

    private final SeqService seqService;

    public CommentsService(CommentsMapper mapper, SeqService seqService) {
        this.mapper = mapper;
        this.seqService = seqService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(Comments bean) {
        bean.setId(seqService.getNextVal(CommentsBase.TABLE_NAME));
        mapper.insert(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Comments bean) {
        mapper.update(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(Integer id) {
        return mapper.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Integer> ids) {
        return ids.stream().filter(Objects::nonNull).mapToInt(this::delete).sum();
    }

    @Nullable
    public Comments select(Integer id) {
        return mapper.select(id);
    }

    public List<Comments> selectList(CommentsArgs args) {
        QueryInfo queryInfo = QueryParser.parse(args.getQueryMap(), CommentsBase.TABLE_NAME, "id_desc");
        return mapper.selectAll(queryInfo);
    }

    public List<Comments> selectList(CommentsArgs args, int offset, int limit) {
        return PageMethod.offsetPage(offset, limit, false).doSelectPage(() -> selectList(args));
    }

    public Page<Comments> selectPage(CommentsArgs args, int page, int pageSize) {
        return PageMethod.startPage(page, pageSize).doSelectPage(() -> selectList(args));
    }
}