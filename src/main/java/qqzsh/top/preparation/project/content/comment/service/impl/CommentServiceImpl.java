package qqzsh.top.preparation.project.content.comment.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.common.utils.aliyun.AliyunTextScanRequest;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.comment.mapper.CommentMapper;
import qqzsh.top.preparation.project.content.comment.domain.Comment;
import qqzsh.top.preparation.project.content.comment.service.ICommentService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.system.user.service.IUserService;

/**
 * 评论Service业务层处理
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Service
public class CommentServiceImpl implements ICommentService 
{
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IMessageService messageService;

    /**
     * 查询评论
     * 
     * @param commentId 评论ID
     * @return 评论
     */
    @Override
    public Comment selectCommentById(Long commentId)
    {
        return commentMapper.selectCommentById(commentId);
    }

    /**
     * 查询评论列表
     * 
     * @param comment 评论
     * @return 评论
     */
    @Override
    public List<Comment> selectCommentList(Comment comment)
    {
        return commentMapper.selectCommentList(comment);
    }

    /**
     * 新增评论
     * 
     * @param comment 评论
     * @return 结果
     */
    @Override
    public int insertComment(Comment comment)
    {
        comment.setDeptId(userService.selectUserById(comment.getCommentUserId()).getDeptId());
        int row = commentMapper.insertComment(comment);
        try {
            review(comment);
        } catch (Exception e) { }
        return row;
    }

    /**
     * 调用阿里接口审核
     * @param comment
     */
    @Async
    public void review(Comment comment) throws Exception {
        String[] split = configService.getKey("aliyun.ai.content").split(";");
        String result = AliyunTextScanRequest.textScanRequest(comment.getCommentContent(), split[0], split[1]);
        Message message = new Message();
        message.setUserId(comment.getCommentUserId());
        message.setSee(0);
        message.setPublishDate(new Date());
        //审核通过
        if("pass".equals(result)){
            comment.setCommentState(1L);
            // 消息模块添加
            message.setContent("【审核通过】您评论的【" + comment.getCommentContent() + "】审核成功！审核人：阿里AI审核员，快去查看吧！");
            messageService.insertMessage(message);
        }else if("block".equals(result)){
            //审核失败
            // 消息模块添加
            message.setContent("【审核失败】您评论的【" + comment.getCommentContent() + "】帖子审核未成功，原因是：内容审核不通过！审核人：阿里AI审核员");
            messageService.insertMessage(message);
        }
        commentMapper.updateComment(comment);
    }

    /**
     * 修改评论
     * 
     * @param comment 评论
     * @return 结果
     */
    @Override
    public int updateComment(Comment comment)
    {
        return commentMapper.updateComment(comment);
    }

    /**
     * 删除评论对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCommentByIds(String ids)
    {
        return commentMapper.deleteCommentByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除评论信息
     * 
     * @param commentId 评论ID
     * @return 结果
     */
    @Override
    public int deleteCommentById(Long commentId)
    {
        return commentMapper.deleteCommentById(commentId);
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        commentMapper.deleteByArticleId(articleId);
    }

    @Override
    public List<Comment> selectJoint(String articleName, String loginName, String beginCommentDate, String endCommentDate,String commentState,Long deptId) {
        return commentMapper.selectJoint(articleName,loginName,beginCommentDate,endCommentDate,commentState,deptId);
    }
}
