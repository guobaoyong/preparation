package qqzsh.top.preparation.project.content.download.domain;

import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.system.user.domain.User;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 用户已下载对象 sys_user_download
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Data
public class UserDownload extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 下载ID */
    private Long downloadId;

    /** 下载时间 */
    @Excel(name = "下载时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date downloadDate;

    /** 下载资源ID */
    @Excel(name = "下载资源ID")
    private Long downloadArticleId;

    /** 下载用户ID */
    @Excel(name = "下载用户ID")
    private Long downloadUserId;

    /** 下载资源、用户 */
    private Article article;
    private User user;
    /** 前台搜索用 */
    private String articleName;
    private String loginName;
}
