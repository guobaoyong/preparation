package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.Message;

import java.util.List;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 12:54
 * @Description 用户消息Service接口
 */
public interface MessageService {

    /**
     * 查询某个用户下的所有消息
     *
     * @param userId
     * @return
     */
    Integer getCountByUserId(Integer userId);

    /**
     * 添加或者修改用户消息
     *
     * @param message
     */
    void save(Message message);

    /**
     * 修改成已查看状态
     *
     * @param userId
     */
    void updateState(Integer userId);

    /**
     * 根据条件分页查询用户消息信息
     *
     * @param s_message
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Message> list(String status, Message s_message, Integer page, Integer pageSize, Sort.Direction direction, String... properties);


    /**
     * 根据条件查询总记录数
     *
     * @param s_message
     * @return
     */
    Long getTotal(String status, Message s_message);

    /**
     * 删除消息
     */
    void delete(Integer id);

    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    Message findById(Integer id);
}

