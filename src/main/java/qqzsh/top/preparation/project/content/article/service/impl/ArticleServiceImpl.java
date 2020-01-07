package qqzsh.top.preparation.project.content.article.service.impl;

import java.util.Date;
import java.util.List;

import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.article.mapper.ArticleMapper;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.article.mapper.ArticleMapper;
import qqzsh.top.preparation.project.content.article.service.IArticleService;

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
    public int insertArticle(Article article) {
        // 将状态设为待审核
        article.setArticleState(0L);
        // 设置发表时间
        article.setArticlePublishDate(new Date());
        // 设置资源所属用户ID
        article.setArticleUserId(ShiroUtils.getSysUser().getUserId());
        return articleMapper.insertArticle(article);
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
    public int deleteArticleByIds(String ids)
    {
        return articleMapper.deleteArticleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除资源信息
     * 
     * @param articleId 资源ID
     * @return 结果
     */
    @Override
    public int deleteArticleById(Long articleId)
    {
        return articleMapper.deleteArticleById(articleId);
    }
}
