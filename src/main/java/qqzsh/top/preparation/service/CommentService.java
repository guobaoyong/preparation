package qqzsh.top.preparation.service;

import qqzsh.top.preparation.entity.Comment;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:56
 * @Description 评论Service接口
 */
public interface CommentService {


    /**
     * 添加或者修改评论
     * @param comment
     */
    public void save(Comment comment);




}

