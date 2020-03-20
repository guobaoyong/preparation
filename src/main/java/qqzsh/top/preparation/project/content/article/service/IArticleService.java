package qqzsh.top.preparation.project.content.article.service;

import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.comment.domain.Comment;

import java.util.List;

/**
 * 资源Service接口
 * 
 * @author zsh
 * @date 2019-12-30
 */
public interface IArticleService {

    /**
     * 查询资源
     * 
     * @param articleId 资源ID
     * @return 资源
     */
    Article selectArticleById(Long articleId);

    /**
     * 查询资源列表
     * 
     * @param article 资源
     * @return 资源集合
     */
    List<Article> selectArticleList(Article article);

    /**
     * 查询资源列表数量
     * @param article
     * @return
     */
    int selectArticleListCount(Article article);

    /**
     * 通过资源ID获取下载量
     * @param articleId
     * @return
     */
    Integer selectDownloadCount(Long articleId);

    /**
     * 新增资源
     * 
     * @param article 资源
     * @return 结果
     */
    int insertArticle(Article article) throws Exception;

    /**
     * 修改资源
     * 
     * @param article 资源
     * @return 结果
     */
    int updateArticle(Article article);

    /**
     * 批量删除资源
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteArticleByIds(String ids);

    /**
     * 删除资源信息
     * 
     * @param articleId 资源ID
     * @return 结果
     */
    int deleteArticleById(Long articleId);

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
