package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.respository.UserRepository;
import qqzsh.top.preparation.service.UserService;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-20 11:35
 * @description 用户Service实现类
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
