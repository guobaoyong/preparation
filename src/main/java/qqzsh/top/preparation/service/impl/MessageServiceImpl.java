package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.Message;
import qqzsh.top.preparation.respository.MessageRepository;
import qqzsh.top.preparation.service.MessageService;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 12:55
 * @Description 用户消息Service实现类
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Integer getCountByUserId(Integer userId) {
        return messageRepository.getCountByUserId(userId);
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

}
