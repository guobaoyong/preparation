package qqzsh.top.preparation.project.content.comment.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.comment.mapper.CommentMapper;
import qqzsh.top.preparation.project.content.comment.domain.Comment;
import qqzsh.top.preparation.project.content.comment.service.ICommentService;
import qqzsh.top.preparation.common.utils.text.Convert;

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
        return commentMapper.insertComment(comment);
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
    public List<Comment> selectJoint(String articleName, String loginName, String beginCommentDate, String endCommentDate,String commentState) {
        return commentMapper.selectJoint(articleName,loginName,beginCommentDate,endCommentDate,commentState);
    }
}
