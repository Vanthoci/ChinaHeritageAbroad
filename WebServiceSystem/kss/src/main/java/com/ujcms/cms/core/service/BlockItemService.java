package com.ujcms.cms.core.service;

import com.github.pagehelper.page.PageMethod;
import com.ujcms.cms.core.domain.BlockItem;
import com.ujcms.cms.core.domain.base.BlockItemBase;
import com.ujcms.cms.core.listener.ChannelDeleteListener;
import com.ujcms.cms.core.listener.SiteDeleteListener;
import com.ujcms.cms.core.mapper.BlockItemMapper;
import com.ujcms.cms.core.service.args.BlockItemArgs;
import com.ujcms.commons.query.QueryInfo;
import com.ujcms.commons.query.QueryParser;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 区块项 Service
 *
 * @author PONY
 */
@Service
public class BlockItemService implements SiteDeleteListener, ChannelDeleteListener {
    private final AttachmentService attachmentService;
    private final BlockItemMapper mapper;
    private final SeqService seqService;

    public BlockItemService(AttachmentService attachmentService, BlockItemMapper mapper, SeqService seqService) {
        this.attachmentService = attachmentService;
        this.mapper = mapper;
        this.seqService = seqService;
    }

    public boolean countByBlockIdAndArticleId(@Param("blockId") Integer blockId, @Param("articleId") Integer articleId) {
        return PageMethod.offsetPage(0, 1, false).<Number>doSelectPage(() ->
                mapper.countByBlockIdAndArticleId(blockId, articleId)).iterator().next().intValue() > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(BlockItem bean) {
        bean.setId(seqService.getNextVal(BlockItemBase.TABLE_NAME));
        mapper.insert(bean);
        attachmentService.insertRefer(BlockItemBase.TABLE_NAME, bean.getId(), bean.getAttachmentUrls());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BlockItem bean) {
        mapper.update(bean);
        attachmentService.updateRefer(BlockItemBase.TABLE_NAME, bean.getId(), bean.getAttachmentUrls());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(List<BlockItem> list) {
        short order = 1;
        for (BlockItem bean : list) {
            bean.setOrder(order);
            mapper.update(bean);
            order += 1;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(Integer id) {
        attachmentService.deleteRefer(BlockItemBase.TABLE_NAME, id);
        return mapper.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Integer> ids) {
        return ids.stream().filter(Objects::nonNull).mapToInt(this::delete).sum();
    }

    @Nullable
    public BlockItem select(Integer id) {
        return mapper.select(id);
    }

    public List<BlockItem> selectList(BlockItemArgs args) {
        QueryInfo queryInfo = QueryParser.parse(args.getQueryMap(), BlockItemBase.TABLE_NAME, "order,id");
        return mapper.selectAll(queryInfo);
    }

    public List<BlockItem> selectList(BlockItemArgs args, int offset, int limit) {
        return PageMethod.offsetPage(offset, limit, false).doSelectPage(() -> selectList(args));
    }

    public boolean existsByBlockId(Integer blockId, Integer notSiteId) {
        return mapper.existsByBlockId(blockId, notSiteId) > 0;
    }

    public int deleteByArticleId(Integer articleId) {
        return mapper.deleteByArticleId(articleId);
    }

    @Override
    public void preChannelDelete(Integer channelId) {
        mapper.deleteByChannelId(channelId);
    }

    @Override
    public void preSiteDelete(Integer siteId) {
        mapper.deleteBySiteId(siteId);
    }

    @Override
    public int deleteListenerOrder() {
        // 栏目(100), 区块(200), 文章(300)、**区块项(400)**
        return 400;
    }
}