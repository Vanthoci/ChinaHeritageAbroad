package com.ujcms.cms.core.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.ujcms.cms.core.domain.Attachment;
import com.ujcms.cms.core.domain.AttachmentRefer;
import com.ujcms.cms.core.domain.base.AttachmentBase;
import com.ujcms.cms.core.domain.base.AttachmentReferBase;
import com.ujcms.cms.core.listener.SiteDeleteListener;
import com.ujcms.cms.core.mapper.AttachmentMapper;
import com.ujcms.cms.core.mapper.AttachmentReferMapper;
import com.ujcms.cms.core.service.args.AttachmentArgs;
import com.ujcms.commons.file.FileHandler;
import com.ujcms.commons.query.QueryInfo;
import com.ujcms.commons.query.QueryParser;
import com.ujcms.commons.web.PathResolver;
import com.ujcms.commons.web.Uploads;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * 附件 Service
 *
 * @author PONY
 */
@Service
public class AttachmentService implements SiteDeleteListener {
    private final AttachmentMapper mapper;
    private final AttachmentReferMapper referMapper;
    private final PathResolver pathResolver;
    private final SeqService seqService;

    public AttachmentService(AttachmentMapper mapper, AttachmentReferMapper referMapper, PathResolver pathResolver,
                             SeqService seqService) {
        this.mapper = mapper;
        this.referMapper = referMapper;
        this.pathResolver = pathResolver;
        this.seqService = seqService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertRefer(String referType, Integer referId, List<String> urls) {
        Set<Integer> ids = doInsertRefer(referType, referId, urls);
        if (!ids.isEmpty()) {
            mapper.updateUsed(ids);
        }
    }

    private Set<Integer> doInsertRefer(String referType, Integer referId, List<String> urls) {
        Set<Integer> ids = new HashSet<>();
        urls.forEach(url -> {
            Attachment attachment = mapper.findByUrl(url);
            if (attachment != null && referMapper.select(attachment.getId(), referType, referId) == null) {
                Long id = seqService.getNextLongVal(AttachmentReferBase.TABLE_NAME);
                referMapper.insert(new AttachmentRefer(id, attachment.getId(), referType, referId));
                ids.add(attachment.getId());
            }
        });
        return ids;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRefer(String referType, Integer referId, List<String> urls) {
        Set<Integer> ids = findAttachmentIds(referType, referId);
        doDeleteRefer(referType, referId);
        if (!urls.isEmpty()) {
            ids.addAll(doInsertRefer(referType, referId, urls));
        }
        if (!ids.isEmpty()) {
            mapper.updateUsed(ids);
        }
    }

    private Set<Integer> findAttachmentIds(String referType, Integer referId) {
        Set<Integer> ids = new HashSet<>();
        referMapper.listByReferTypeAndReferId(referType, referId).forEach(refer -> ids.add(refer.getAttachmentId()));
        return ids;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRefer(String referType, Integer referId) {
        Set<Integer> ids = findAttachmentIds(referType, referId);
        doDeleteRefer(referType, referId);
        if (!ids.isEmpty()) {
            mapper.updateUsed(ids);
        }
    }

    private void doDeleteRefer(String referType, Integer referId) {
        referMapper.deleteByReferTypeAndReferId(referType, referId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(Attachment bean) {
        bean.setId(seqService.getNextVal(AttachmentBase.TABLE_NAME));
        mapper.insert(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Attachment bean) {
        mapper.update(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(Integer id) {
        Attachment bean = select(id);
        if (bean != null) {
            return delete(bean);
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(Attachment bean) {
        if (Boolean.FALSE.equals(bean.getUsed())) {
            FileHandler fileHandler = bean.getSite().getConfig().getUploadStorage().getFileHandler(pathResolver);
            Optional.ofNullable(fileHandler.getName(bean.getUrl())).ifPresent(pathname -> {
                fileHandler.deleteFileAndEmptyParentDir(pathname);
                // 删除缩略图，如果有的话
                fileHandler.deleteFileAndEmptyParentDir(Uploads.getThumbnailName(pathname));
            });
        }
        return mapper.delete(bean.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Integer> ids) {
        return ids.stream().filter(Objects::nonNull).mapToInt(this::delete).sum();
    }

    @Nullable
    public Attachment select(Integer id) {
        return mapper.select(id);
    }

    public List<Attachment> selectList(AttachmentArgs args) {
        QueryInfo queryInfo = QueryParser.parse(args.getQueryMap(), AttachmentBase.TABLE_NAME, "id_desc");
        return mapper.selectAll(queryInfo);
    }

    public List<Attachment> selectList(AttachmentArgs args, int offset, int limit) {
        return PageMethod.offsetPage(offset, limit, false).doSelectPage(() -> selectList(args));
    }

    public Page<Attachment> selectPage(AttachmentArgs args, int page, int pageSize) {
        return PageMethod.startPage(page, pageSize).doSelectPage(() -> selectList(args));
    }

    /**
     * 统计附件数量
     *
     * @param siteId  站点ID
     * @param created 创建日期
     * @return 附件数量
     */
    public int countByCreated(Integer siteId, @Nullable OffsetDateTime created) {
        return mapper.countByCreated(siteId, created);
    }

    @Override
    public void preSiteDelete(Integer siteId) {
        referMapper.deleteBySiteId(siteId);
        mapper.delete(siteId);
    }

    @Override
    public int deleteListenerOrder() {
        return 100;
    }
}