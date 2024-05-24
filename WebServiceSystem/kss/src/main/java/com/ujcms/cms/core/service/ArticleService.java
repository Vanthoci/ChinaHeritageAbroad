package com.ujcms.cms.core.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.ImmutableMap;
import com.ujcms.cms.core.domain.*;
import com.ujcms.cms.core.domain.base.ArticleBase;
import com.ujcms.cms.core.domain.base.ArticleCustomBase;
import com.ujcms.cms.core.generator.HtmlService;
import com.ujcms.cms.core.listener.ChannelDeleteListener;
import com.ujcms.cms.core.listener.SiteDeleteListener;
import com.ujcms.cms.core.listener.UserDeleteListener;
import com.ujcms.cms.core.lucene.ArticleLucene;
import com.ujcms.cms.core.lucene.ArticleLuceneArgs;
import com.ujcms.cms.core.lucene.domain.EsArticle;
import com.ujcms.cms.core.mapper.ArticleCustomMapper;
import com.ujcms.cms.core.mapper.ArticleExtMapper;
import com.ujcms.cms.core.mapper.ArticleMapper;
import com.ujcms.cms.core.mapper.ArticleTagMapper;
import com.ujcms.cms.core.service.args.ArticleArgs;
import com.ujcms.commons.db.order.OrderEntityUtils;
import com.ujcms.commons.query.CustomFieldQuery;
import com.ujcms.commons.query.OffsetLimitRequest;
import com.ujcms.commons.query.QueryInfo;
import com.ujcms.commons.query.QueryParser;
import com.ujcms.commons.web.exception.LogicException;
import info.debatty.java.stringsimilarity.JaroWinkler;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.owasp.html.PolicyFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 自己加的
import com.ujcms.cms.core.mapper.BrowserMapper;
// 

import java.time.OffsetDateTime;
import java.util.*;

import static com.ujcms.cms.core.domain.Article.*;

/**
 * 文章 Service
 *
 * @author PONY
 */
@Service
public class ArticleService implements ChannelDeleteListener, UserDeleteListener, SiteDeleteListener {
    private final HtmlService htmlService;
    private final PolicyFactory policyFactory;
    private final RuntimeService runtimeService;
    private final ChannelService channelService;
    private final TagService tagService;
    private final BlockItemService blockItemService;
    private final ArticleLucene articleLucene;
    private final ArticleMapper mapper;
    private final ArticleExtMapper extMapper;
    private final ArticleCustomMapper customMapper;
    private final ArticleTagMapper articleTagMapper;
    private final AttachmentService attachmentService;
    private final SeqService seqService;
    private final BrowserMapper browserMapper;

    public ArticleService(HtmlService htmlService, PolicyFactory policyFactory, RuntimeService runtimeService,
            ChannelService channelService, TagService tagService, BlockItemService blockItemService,
            ArticleLucene articleLucene, ArticleMapper mapper, ArticleExtMapper extMapper,
            ArticleCustomMapper customMapper, ArticleTagMapper articleTagMapper,
            AttachmentService attachmentService, SeqService seqService, BrowserMapper browserMapper) {
        this.htmlService = htmlService;
        this.policyFactory = policyFactory;
        this.runtimeService = runtimeService;
        this.channelService = channelService;
        this.tagService = tagService;
        this.blockItemService = blockItemService;
        this.articleLucene = articleLucene;
        this.mapper = mapper;
        this.extMapper = extMapper;
        this.customMapper = customMapper;
        this.articleTagMapper = articleTagMapper;
        this.attachmentService = attachmentService;
        this.seqService = seqService;
        this.browserMapper = browserMapper;
    }

    private void insertRelatedList(Integer articleId, Integer siteId, Integer userId,
            List<String> tagNames, List<ArticleCustom> customList) {
        short tagOrder = 0;
        for (String name : tagNames) {
            Tag tag = tagService.selectByName(siteId, name);
            if (tag == null) {
                tag = new Tag(siteId, userId, name);
                tag.setRefers(1);
                tagService.insert(tag);
            } else {
                tag.setRefers(tag.getRefers() + 1);
                tagService.update(tag);
            }
            articleTagMapper.insert(new ArticleTag(articleId, tag.getId(), tagOrder++));
        }
        customList.forEach(it -> {
            it.setId(seqService.getNextLongVal(ArticleCustomBase.TABLE_NAME));
            it.setArticleId(articleId);
            if (it.isRichEditor()) {
                it.setValue(policyFactory.sanitize(it.getValue()));
            }
            customMapper.insert(it);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(Article bean, ArticleExt ext, Integer userId, Integer orgId,
            List<String> tagNames, Map<String, Object> customs) {
        bean.setUserId(userId);
        bean.setModifiedUserId(userId);
        bean.setOrgId(orgId);
        OffsetDateTime now = OffsetDateTime.now();
        bean.setCreated(now);
        bean.setModified(now);
        bean.setId(seqService.getNextVal(ArticleBase.TABLE_NAME));
        // 以当前时间戳为排序值
        bean.setOrder(System.currentTimeMillis());
        bean.setWithImage(StringUtils.isNotBlank(ext.getImage()));
        Channel channel = channelService.select(bean.getChannelId());
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found. bean.channelId: " + bean.getChannelId());
        }
        String processKey = channel.getProcessKey();
        if (bean.getStatus() == Article.STATUS_PUBLISHED && StringUtils.isNotBlank(processKey)) {
            startProcess(bean, processKey, userId);
        }
        ext.setId(bean.getId());
        ext.setText(policyFactory.sanitize(ext.getText()));
        mapper.insert(bean);
        extMapper.insert(ext);
        List<ArticleCustom> customList = Article.disassembleCustoms(channel.getArticleModel(), bean.getId(), customs);
        insertRelatedList(bean.getId(), bean.getSiteId(), userId, tagNames, customList);
        attachmentService.insertRefer(ArticleBase.TABLE_NAME, bean.getId(), bean.getAttachmentUrls());
        Optional.ofNullable(select(bean.getId())).ifPresent(article -> articleLucene.save(EsArticle.of(article)));
    }

    private void startProcess(Article article, String processKey, Integer userId) {
        try {
            Authentication.setAuthenticatedUserId(userId.toString());
            ProcessInstance instance = runtimeService.startProcessInstanceByKeyAndTenantId(
                    processKey, article.getId().toString(),
                    ImmutableMap.of(
                            PROCESS_VARIABLE_CHANNEL_ID, article.getChannelId(),
                            PROCESS_VARIABLE_ORG_ID, article.getOrgId(),
                            PROCESS_VARIABLE_USER_ID, article.getUserId()),
                    article.getSiteId().toString());
            article.setStatus(Article.STATUS_PENDING);
            article.setProcessInstanceId(instance.getProcessInstanceId());
        } finally {
            Authentication.setAuthenticatedUserId(null);
        }
    }

    private void deleteProcessIfNecessary(Article article, User user) {
        short status = article.getStatus();
        String processInstanceId = article.getProcessInstanceId();
        // 待审核和审核中的，要删除流程
        boolean hasProcessInstanceId = (status == STATUS_PENDING || status == STATUS_REVIEWING)
                && StringUtils.isNotBlank(processInstanceId);
        if (hasProcessInstanceId &&
                runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).count() > 0) {
            runtimeService.deleteProcessInstance(processInstanceId, user.getUsername() + ":cancel");
        }
    }

    /**
     * 提交。草稿、待审核、已归档、已退回、已下线、已删除，可以提交。
     */
    @Transactional(rollbackFor = Exception.class)
    public void submit(Article bean, Integer userId) {
        String processKey = bean.getChannel().getProcessKey();
        short status = bean.getStatus();
        if (status == STATUS_DRAFT || status == STATUS_PENDING || status == STATUS_ARCHIVED
                || status == STATUS_REJECTED || status == STATUS_OFFLINE || status == STATUS_DELETED) {
            if (StringUtils.isNotBlank(processKey)) {
                // 有流程且在待审核状态无需处理，直接返回
                if (status == STATUS_PENDING) {
                    return;
                }
                startProcess(bean, processKey, userId);
            } else {
                bean.setStatus(STATUS_PUBLISHED);
                bean.setProcessInstanceId(null);
            }
            update(bean, bean.getExt());
            Optional.ofNullable(select(bean.getId())).ifPresent(article -> articleLucene.update(EsArticle.of(article)));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void archive(Article bean) {
        short status = bean.getStatus();
        if (status == STATUS_PUBLISHED) {
            bean.setStatus(STATUS_ARCHIVED);
            update(bean);
            Optional.ofNullable(select(bean.getId())).ifPresent(article -> articleLucene.update(EsArticle.of(article)));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void offline(Article bean, User user) {
        short status = bean.getStatus();
        if (status == STATUS_PUBLISHED || status == STATUS_ARCHIVED || status == STATUS_READY
                || status == STATUS_PENDING || status == STATUS_REVIEWING) {
            deleteProcessIfNecessary(bean, user);
            bean.setStatus(STATUS_OFFLINE);
            update(bean);
            Optional.ofNullable(select(bean.getId())).ifPresent(article -> articleLucene.update(EsArticle.of(article)));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Article bean, User user) {
        deleteProcessIfNecessary(bean, user);
        bean.setStatus(STATUS_DELETED);
        update(bean);
        Optional.ofNullable(select(bean.getId())).ifPresent(article -> articleLucene.update(EsArticle.of(article)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Article bean, ArticleExt ext, Integer userId,
            List<String> tagNames, Map<String, Object> customs) {
        bean.setModifiedUserId(userId);
        bean.setWithImage(StringUtils.isNotBlank(ext.getImage()));
        bean.setModified(OffsetDateTime.now());
        ext.setText(policyFactory.sanitize(ext.getText()));
        mapper.update(bean);
        extMapper.update(ext);
        bean.setExt(ext);
        Channel channel = Optional.ofNullable(channelService.select(bean.getChannelId()))
                .orElseThrow(() -> new IllegalArgumentException("bean.channelId cannot be null"));
        List<ArticleCustom> customList = Article.disassembleCustoms(channel.getArticleModel(), bean.getId(), customs);
        // 要先将修改后的数据放入bean中，否则bean.getAttachmentUrls()会获取修改前的值
        bean.setCustomList(customList);
        customMapper.deleteByArticleId(bean.getId());
        tagService.reduceReferByArticleId(bean.getId());
        articleTagMapper.deleteByArticleId(bean.getId());
        insertRelatedList(bean.getId(), bean.getSiteId(), userId, tagNames, customList);
        attachmentService.updateRefer(ArticleBase.TABLE_NAME, bean.getId(), bean.getAttachmentUrls());
        Optional.ofNullable(select(bean.getId())).ifPresent(article -> articleLucene.update(EsArticle.of(article)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Article bean) {
        mapper.update(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ArticleExt ext) {
        extMapper.update(ext);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Article bean, ArticleExt ext) {
        mapper.update(bean);
        extMapper.update(ext);
    }

    @Transactional(rollbackFor = Exception.class)
    public void moveOrder(Integer fromId, Integer toId) {
        OrderEntityUtils.move(mapper, fromId, toId);
    }

    @Transactional(rollbackFor = Exception.class)
    public int completelyDelete(Article bean, User user) {
        for (Article dest : bean.getDestList()) {
            // 引用和映射，则直接删除
            if (dest.getType() == TYPE_MAP || dest.getType() == TYPE_REFER) {
                completelyDelete(dest, user);
            }
            // 复制或其它，则清空srcId
            else {
                dest.setSrcId(null);
                update(dest);
            }
        }
        deleteProcessIfNecessary(bean, user);
        attachmentService.deleteRefer(ArticleBase.TABLE_NAME, bean.getId());
        extMapper.delete(bean.getId());
        customMapper.deleteByArticleId(bean.getId());
        tagService.reduceReferByArticleId(bean.getId());
        articleTagMapper.deleteByArticleId(bean.getId());
        blockItemService.deleteByArticleId(bean.getId());
        int count = mapper.delete(bean.getId());
        articleLucene.deleteById(bean.getId());
        htmlService.deleteArticleHtml(bean);
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Article> listAndUpdateStickyDate() {
        OffsetDateTime now = OffsetDateTime.now();
        ArticleArgs args = ArticleArgs.of()
                .geSticky((short) 1)
                .leStickyDate(now);
        List<Article> articles = selectList(args);
        mapper.updateStickyDate(now);
        return articles;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Article> listAndUpdateOnlineStatus() {
        OffsetDateTime now = OffsetDateTime.now();
        ArticleArgs args = ArticleArgs.of()
                .status(Collections.singleton(STATUS_READY))
                .geOnlineDateOrNull(now);
        List<Article> articles = selectList(args);
        for (Article article : articles) {
            article.setStatus(STATUS_PUBLISHED);
        }
        mapper.updateOnlineStatus(now, STATUS_READY, STATUS_PUBLISHED);
        return articles;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Article> listAndUpdateOfflineStatus() {
        OffsetDateTime now = OffsetDateTime.now();
        List<Short> normals = Arrays.asList(STATUS_PUBLISHED, STATUS_ARCHIVED);
        ArticleArgs args = ArticleArgs.of()
                .status(normals)
                .geOfflineDate(now);
        List<Article> articles = selectList(args);
        for (Article article : articles) {
            article.setStatus(STATUS_OFFLINE);
        }
        mapper.updateOfflineStatus(now, normals, STATUS_OFFLINE);
        return articles;
    }

    @Nullable
    public Article select(Integer id) {
        return mapper.select(id);
    }

    // ----------------------------------
    // 增加 Browser 类，返回 Article
    public List<Article> selectArticles(String cons) {
        return browserMapper.selectArticles(cons);
    }

    // ----------------------------------
    public List<Article> selectList(ArticleArgs args) {
        QueryInfo queryInfo = QueryParser.parse(args.getQueryMap(), ArticleBase.TABLE_NAME, "order_desc");
        List<QueryInfo.WhereCondition> customsCondition = CustomFieldQuery.parse(args.getCustomsQueryMap());
        return mapper.selectAll(queryInfo, customsCondition, args.getOrgAncestorId(), args.getChannelAncestorIds(),
                args.getRoleIds());
    }

    public long count(ArticleArgs args) {
        return PageMethod.count(() -> selectList(args));
    }

    public List<Article> selectList(ArticleArgs args, int offset, int limit) {
        return PageMethod.offsetPage(offset, limit, false).doSelectPage(() -> selectList(args));
    }

    public Page<Article> selectPage(ArticleArgs args, int page, int pageSize) {
        return PageMethod.startPage(page, pageSize).doSelectPage(() -> selectList(args));
    }

    public List<Article> listByIds(Collection<Integer> ids) {
        return mapper.listByIds(ids);
    }

    public List<Article> selectByMinId(Integer minId, @Nullable Integer siteId, @Nullable Integer subChannelId,
            int offset, int limit) {
        ArticleArgs args = ArticleArgs.of().siteId(siteId).channelAncestorId(subChannelId).minId(minId)
                .status(Arrays.asList(STATUS_PUBLISHED, STATUS_ARCHIVED)).orderById();
        return selectList(args, offset, limit);
    }

    public List<Article> listBySiteIdForSiteMap(Integer siteId, @Nullable Integer minId, int limit) {
        return PageMethod.offsetPage(0, limit, false).doSelectPage(() -> mapper.listBySiteIdForSitemap(siteId, minId));
    }

    @Nullable
    public Article findNext(Integer id, Long order, Integer channelId) {
        List<Article> list = PageMethod.offsetPage(0, 1, false).doSelectPage(
                () -> mapper.findNext(id, order, channelId, Arrays.asList(STATUS_PUBLISHED, STATUS_ARCHIVED)));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Nullable
    public Article findPrev(Integer id, Long order, Integer channelId) {
        List<Article> list = PageMethod.offsetPage(0, 1, false).doSelectPage(
                () -> mapper.findPrev(id, order, channelId, Arrays.asList(STATUS_PUBLISHED, STATUS_ARCHIVED)));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<EsArticle> titleSimilarity(double similarity, String title, Integer siteId,
            @Nullable Integer excludeId) {
        ArticleLuceneArgs args = new ArticleLuceneArgs();
        args.setTitle(title);
        args.setSiteId(siteId);
        if (excludeId != null) {
            args.setExcludeIds(Collections.singleton(excludeId));
        }
        // 获取得分最高的10条记录
        List<EsArticle> list = articleLucene.findAll(args, null, new OffsetLimitRequest(0, 10))
                .getContent();
        List<EsArticle> result = new ArrayList<>();
        JaroWinkler jaroWinkler = new JaroWinkler();
        for (EsArticle bean : list) {
            double curr = jaroWinkler.similarity(title, bean.getTitle());
            // 返回相似度大于 60% 的数据
            if (curr > similarity) {
                bean.setScore(curr);
                result.add(bean);
            }
        }
        result.sort((o1, o2) -> {
            if (o1.getScore() == o2.getScore()) {
                return 0;
            }
            return o1.getScore() - o2.getScore() < 0 ? 1 : 0;
        });
        return result;
    }

    public int countBySiteId(Integer siteId) {
        return mapper.countBySiteId(siteId, Collections.emptyList());
    }

    /**
     * 统计文章数量
     *
     * @param siteId      站点ID
     * @param publishDate 发布日期
     * @param status      状态
     * @return 文章数量
     */
    public int countByPublishDate(Integer siteId, @Nullable OffsetDateTime publishDate, Collection<Short> status) {
        return mapper.countByPublishDate(siteId, publishDate, status);
    }

    public Map<String, Object> statForSitemap(Integer siteId, int limit) {
        List<Map<String, Object>> list = PageMethod.offsetPage(0, limit, false)
                .doSelectPage(() -> mapper.statForSitemap(siteId, Collections.emptyList()));
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }
        return list.get(0);
    }

    public boolean existsByChannelId(Integer channelId) {
        return mapper.existsByChannelId(channelId) > 0;
    }

    public boolean existsByUserId(Integer userId) {
        return mapper.existsByUserId(userId) > 0;
    }

    @Override
    public void preUserDelete(Integer userId) {
        if (existsByUserId(userId)) {
            throw new LogicException("error.refer.article");
        }
        mapper.updateModifiedUser(userId, User.ANONYMOUS_ID);
    }

    @Override
    public void preChannelDelete(Integer channelId) {
        if (existsByChannelId(channelId)) {
            throw new LogicException("error.refer.article");
        }
    }

    @Override
    public void preSiteDelete(Integer siteId) {
        extMapper.deleteBySiteId(siteId);
        articleTagMapper.deleteBySiteId(siteId);
        customMapper.deleteBySiteId(siteId);
        mapper.deleteBySiteId(siteId);
    }

    @Override
    public int deleteListenerOrder() {
        // 栏目(100), 区块(200), **文章(300)**、区块项(400)
        return 300;
    }
}