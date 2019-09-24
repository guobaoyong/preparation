package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.User;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-20 11:34
 * @description 用户Service接口
 */
public interface UserService {

    /**
     * 添加或者修改用户信息
     * @param user
     */
    void save(User user);

    /**
     * 根据用户名查找用户实体
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * 根据邮箱查找用户实体
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 根据id查找实体
     * @param id
     * @return
     */
    User getById(Integer id);

    /**
     * 根据条件分页查询用户信息
     * @param s_user
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<User> list(User s_user, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    /**
     * 根据条件查询总记录数
     * @param s_user
     * @return
     */
    Long getTotal(User s_user);

}
