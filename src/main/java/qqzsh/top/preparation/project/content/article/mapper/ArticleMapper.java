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
     * 查询资源列表数量
     * @param article
     * @return
     */
    int selectArticleListCount(Article article);

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

    /**
     * 获取最新10条资源内容
     * @return
     */
    List<Article> selectNew10();

    /**
     * 获取最热10条资源内容
     * @return
     */
    List<Article> selectHot10();

    /**
     * 获取下载量最大的10条资源
     * @return
     */
    List<Article> selectDownloadTop10();
}
