package qqzsh.top.preparation.project.content.article.mapper;

import qqzsh.top.preparation.project.content.article.domain.Article;
import java.util.List;

/**
 * 资源Mapper接口
 * 
 * @author zsh
 * @date 2019-12-30
 */
public interface ArticleMapper 
{
    /**
     * 查询资源
     * 
     * @param articleId 资源ID
     * @return 资源
     */
    public Article selectArticleById(Long articleId);

    /**
     * 查询资源列表
     * 
     * @param article 资源
     * @return 资源集合
     */
    public List<Article> selectArticleList(Article article);

    /**
     * 新增资源
     * 
     * @param article 资源
     * @return 结果
     */
    public int insertArticle(Article article);

    /**
     * 修改资源
     * 
     * @param article 资源
     * @return 结果
     */
    public int updateArticle(Article article);

    /**
     * 删除资源
     * 
     * @param articleId 资源ID
     * @return 结果
     */
    public int deleteArticleById(Long articleId);

    /**
     * 批量删除资源
     * 
     * @param articleIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteArticleByIds(String[] articleIds);
}
