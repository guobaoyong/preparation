package qqzsh.top.preparation.service;

import qqzsh.top.preparation.entity.Message;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 12:54
 * @Description 用户消息Service接口
 */
public interface MessageService {

    /**
     * 查询某个用户下的所有消息
     * @param userId
     * @return
     */
    Integer getCountByUserId(Integer userId);

    /**
     * 添加或者修改用户消息
     * @param message
     */
    void save(Message message);
}

