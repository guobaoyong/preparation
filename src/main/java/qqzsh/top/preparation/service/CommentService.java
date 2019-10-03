package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.Comment;

import java.util.List;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:56
 * @Description 评论Service接口
 */
public interface CommentService {

    /**
     * 添加或者修改评论
     *
     * @param comment
     */
    void save(Comment comment);

    /**
     * 根据条件分页查询评论信息
     *
     * @param s_comment
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Comment> list(Comment s_comment, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    /**
     * 根据条件获取总记录数
     *
     * @param s_comment
     * @return
     */
    Long getTotal(Comment s_comment);

    /**
     * 删除评论
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    Comment get(Integer id);

    /**
     * 删除指定帖子的评论信息
     *
     * @param articleId
     */
    void deleteByArticleId(Integer articleId);

    /**
     * 根据用户id删除
     */
    void deleteByUserId(Integer userId);
}

