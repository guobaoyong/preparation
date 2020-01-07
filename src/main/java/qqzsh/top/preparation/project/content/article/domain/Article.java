package qqzsh.top.preparation.project.content.article.domain;

import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.system.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import qqzsh.top.preparation.project.content.type.domain.ArcType;

import java.util.Date;

/**
 * 资源对象 sys_article
 * 
 * @author zsh
 * @date 2019-12-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 资源ID */
    private Long articleId;

    /** 审核时间 */
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date articleCheckDate;

    /** 内容 */
    @Excel(name = "内容")
    private String articleContent;

    /** 下载地址 */
    @Excel(name = "下载地址")
    private String articleDownload1;

    /** 是否是热门资源 */
    @Excel(name = "是否是热门资源")
    private boolean articleIsHot;

    /** 名称 */
    @Excel(name = "名称")
    private String articleName;

    /** 密码 */
    @Excel(name = "密码")
    private String articlePassword1;

    /** 积分数 */
    @Excel(name = "积分数")
    private Integer articlePoints;

    /** 发布时间 */
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date articlePublishDate;

    /** 拒绝原因 */
    @Excel(name = "拒绝原因")
    private String articleReason;

    /** 状态 */
    @Excel(name = "状态")
    private Long articleState;

    /** 查看次数 */
    @Excel(name = "查看次数")
    private Long articleView;

    /** 资源所属类别ID */
    @Excel(name = "资源所属类别ID")
    private Long articleTypeId;

    /** 资源所属类别 */
    private ArcType arcType;

    /** 资源所属用户ID */
    @Excel(name = "资源所属用户ID")
    private Long articleUserId;

    /** 资源所属用户 */
    private User user;

}
