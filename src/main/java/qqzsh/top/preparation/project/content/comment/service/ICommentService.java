package qqzsh.top.preparation.project.content.comment.service;

import qqzsh.top.preparation.project.content.comment.domain.Comment;
import java.util.List;

/**
 * 评论Service接口
 * 
 * @author zsh
 * @date 2019-12-31
 */
public interface ICommentService {

    /**
     * 查询评论
     * 
     * @param commentId 评论ID
     * @return 评论
     */
    public Comment selectCommentById(Long commentId);

    /**
     * 查询评论列表
     * 
     * @param comment 评论
     * @return 评论集合
     */
    public List<Comment> selectCommentList(Comment comment);

    /**
     * 新增评论
     * 
     * @param comment 评论
     * @return 结果
     */
    public int insertComment(Comment comment);

    /**
     * 修改评论
     * 
     * @param comment 评论
     * @return 结果
     */
    public int updateComment(Comment comment);

    /**
     * 批量删除评论
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCommentByIds(String ids);

    /**
     * 删除评论信息
     * 
     * @param commentId 评论ID
     * @return 结果
     */
    public int deleteCommentById(Long commentId);

    /**
     * 删除指定帖子的评论信息
     *
     * @param articleId
     */
    void deleteByArticleId(Long articleId);

    /**
     * 联表查询
     * @param articleName
     * @param loginName
     * @return
     */
    List<Comment> selectJoint(String articleName, String loginName,String beginCommentDate,String endCommentDate,String commentState,Long deptId);
}
