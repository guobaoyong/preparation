package qqzsh.top.preparation.service;

import qqzsh.top.preparation.entity.User;

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
    public User getById(Integer id);

}
