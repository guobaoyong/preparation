package qqzsh.top.preparation.project.content.message.service;

import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.message.domain.Message;
import java.util.List;

/**
 * 消息Service接口
 * 
 * @author zsh
 * @date 2020-01-02
 */
public interface IMessageService 
{
    /**
     * 查询消息
     * 
     * @param id 消息ID
     * @return 消息
     */
    public Message selectMessageById(Long id);

    /**
     * 查询消息列表
     * 
     * @param message 消息
     * @return 消息集合
     */
    public List<Message> selectMessageList(Message message);

    /**
     * 新增消息
     * 
     * @param message 消息
     * @return 结果
     */
    public int insertMessage(Message message);

    /**
     * 修改消息
     * 
     * @param message 消息
     * @return 结果
     */
    public int updateMessage(Message message);

    /**
     * 批量删除消息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMessageByIds(String ids);

    /**
     * 删除消息信息
     * 
     * @param id 消息ID
     * @return 结果
     */
    public int deleteMessageById(Long id);

    /**
     * 修改成已查看状态
     *
     * @param userId
     */
    void updateState(Long userId);

    /**
     * 联表查询
     * @param loginName
     * @return
     */
    List<Message> selectJoint(String content,String loginName, String beginPublishDate, String endPublishDate,Long deptId);
}
