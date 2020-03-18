package qqzsh.top.preparation.project.content.comment.domain;

import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.system.user.domain.User;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 评论对象 sys_comment
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Data
public class Comment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long commentId;

    /** 评论时间 */
    @Excel(name = "评论时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date commentDate;

    /** 内容 */
    @Excel(name = "内容")
    private String commentContent;

    /** 状态 */
    @Excel(name = "状态")
    private Long commentState;

    /** 资源ID */
    @Excel(name = "资源ID")
    private Long commentArticleId;

    /** 评论人ID */
    @Excel(name = "评论人ID")
    private Long commentUserId;

    private User user;
    private Article article;
    /** 前台搜索用 */
    private String articleName;
    private String loginName;

    private Long deptId;
}
