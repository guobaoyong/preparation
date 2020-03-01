package qqzsh.top.preparation.project.content.article.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.common.utils.aliyun.AliyunTextScanRequest;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.article.mapper.ArticleMapper;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.article.mapper.ArticleMapper;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.front.lucene.ArticleIndex;

/**
 * 资源Service业务层处理
 * 
 * @author zsh
 * @date 2019-12-30
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private ArticleIndex articleIndex;

    /**
     * 查询资源
     * 
     * @param articleId 资源ID
     * @return 资源
     */
    @Override
    public Article selectArticleById(Long articleId)
    {
        return articleMapper.selectArticleById(articleId);
    }

    /**
     * 查询资源列表
     * 
     * @param article 资源
     * @return 资源
     */
    @Override
    public List<Article> selectArticleList(Article article)
    {
        return articleMapper.selectArticleList(article);
    }

    /**
     * 新增资源
     * 
     * @param article 资源
     * @return 结果
     */
    @Override
    public int insertArticle(Article article) throws Exception {
        // 将状态设为待审核
        article.setArticleState(0L);
        if (article.getArticlePublishDate() == null){
            // 设置发表时间
            article.setArticlePublishDate(new Date());
        }
        // 设置资源所属用户ID
        if (article.getArticleUserId() == null){
            article.setArticleUserId(ShiroUtils.getSysUser().getUserId());
        }
        int row = articleMapper.insertArticle(article);
        if (redisUtil.hasKey("articleNums")){
            Integer integer =  (Integer) redisUtil.get("articleNums");
            redisUtil.delete("articleNums");
            redisUtil.set("articleNums",integer+1);
        }else {
            redisUtil.set("articleNums",selectArticleList(new Article()).size());
        }
        //调用异步接口
        review(article);
        return row;
    }

    /**
     * 调用阿里接口审核
     * @param article
     */
    @Async
    public void review(Article article) throws Exception {
        String[] split = configService.getKey("aliyun.ai.content").split(";");
        String result = AliyunTextScanRequest.textScanRequest(article.getArticleContent(), split[0], split[1]);
        // 更新时间
        article.setArticleCheckDate(new Date());
        article.setArticleView(0L);
        Message message = new Message();
        message.setUserId(articleMapper.selectArticleById(article.getArticleId()).getArticleUserId());
        message.setSee(0);
        message.setPublishDate(new Date());
        //审核通过
        if("pass".equals(result)){
            article.setArticleState(1L);
            // 消息模块添加
            message.setContent("【审核通过】您发布的【" + article.getArticleName() + "】帖子审核成功！审核人：阿里AI审核员");
            messageService.insertMessage(message);
            // 帖子加入索引
            try {
                articleIndex.addIndex(article);
            }catch (Exception e){
                articleIndex.updateIndex(article);
            }
        }else if("block".equals(result)){
            //审核失败
            // 消息模块添加
            message.setContent("【审核失败】您发布的【" + article.getArticleName() + "】帖子审核未成功，原因是：内容审核不通过！审核人：阿里AI审核员");
            messageService.insertMessage(message);
        }
        articleMapper.updateArticle(article);
    }

    /**
     * 修改资源
     * 
     * @param article 资源
     * @return 结果
     */
    @Override
    public int updateArticle(Article article) {
        return articleMapper.updateArticle(article);
    }

    /**
     * 删除资源对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteArticleByIds(String ids) {
        int row = articleMapper.deleteArticleByIds(Convert.toStrArray(ids));
        if (redisUtil.hasKey("articleNums")){
            Integer integer =  (Integer) redisUtil.get("articleNums");
            redisUtil.delete("articleNums");
            redisUtil.set("articleNums",integer-row);
        }else {
            redisUtil.set("articleNums",selectArticleList(new Article()).size());
        }
        return row;
    }

    /**
     * 删除资源信息
     * 
     * @param articleId 资源ID
     * @return 结果
     */
    @Override
    public int deleteArticleById(Long articleId) {
        int row = articleMapper.deleteArticleById(articleId);
        if (redisUtil.hasKey("articleNums")){
            Integer integer =  (Integer) redisUtil.get("articleNums");
            redisUtil.delete("articleNums");
            redisUtil.set("articleNums",integer-1);
        }else {
            redisUtil.set("articleNums",selectArticleList(new Article()).size());
        }
        return row;
    }
}
