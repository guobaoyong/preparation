package qqzsh.top.preparation.project.content.message.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.message.mapper.MessageMapper;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.mapper.MessageMapper;
import qqzsh.top.preparation.project.content.message.service.IMessageService;

/**
 * 消息Service业务层处理
 * 
 * @author zsh
 * @date 2020-01-02
 */
@Service
public class MessageServiceImpl implements IMessageService
{
    @Autowired
    private MessageMapper messageMapper;

    /**
     * 查询消息
     * 
     * @param id 消息ID
     * @return 消息
     */
    @Override
    public Message selectMessageById(Long id)
    {
        return messageMapper.selectMessageById(id);
    }

    /**
     * 查询消息列表
     * 
     * @param message 消息
     * @return 消息
     */
    @Override
    public List<Message> selectMessageList(Message message)
    {
        return messageMapper.selectMessageList(message);
    }

    /**
     * 新增消息
     * 
     * @param message 消息
     * @return 结果
     */
    @Override
    public int insertMessage(Message message)
    {
        return messageMapper.insertMessage(message);
    }

    /**
     * 修改消息
     * 
     * @param message 消息
     * @return 结果
     */
    @Override
    public int updateMessage(Message message)
    {
        return messageMapper.updateMessage(message);
    }

    /**
     * 删除消息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMessageByIds(String ids)
    {
        return messageMapper.deleteMessageByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除消息信息
     * 
     * @param id 消息ID
     * @return 结果
     */
    @Override
    public int deleteMessageById(Long id)
    {
        return messageMapper.deleteMessageById(id);
    }

    @Override
    public void updateState(Long userId) {
        messageMapper.updateState(userId);
    }

    @Override
    public List<Message> selectJoint(String content,String loginName, String beginPublishDate, String endPublishDate) {
        return messageMapper.selectJoint(content,loginName, beginPublishDate, endPublishDate);
    }
}
