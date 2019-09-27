package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.Article;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:40
 * @description 资源Service接口
 */
public interface ArticleService {

    /**
     * 根据条件分页查询资源信息
     * @param s_article
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Article> list(Article s_article, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    /**
     * 查询所有帖子
     * @return
     */
    List<Article> listAll();

    /**
     * 根据条件查询总记录数
     * @param s_article
     * @return
     */
    Long getTotal(Article s_article);

    /**
     * 根据id获取实体
     * @param id
     * @return
     */
    Article get(Integer id);

    /**
     * 添加或者修改帖子
     * @param article
     */
    void save(Article article);

    /**
     * 根据id删除帖子
     * @param id
     */
    void delete(Integer id);


}
