package qqzsh.top.preparation.project.content.message.domain;

import qqzsh.top.preparation.project.system.user.domain.User;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import qqzsh.top.preparation.project.system.user.domain.User;

import java.util.Date;

/**
 * 消息对象 sys_message
 * 
 * @author zsh
 * @date 2020-01-02
 */
@Data
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long id;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 状态 */
    @Excel(name = "状态")
    private Integer see;

    /** 发布时间 */
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishDate;

    /** 接收用户ID */
    @Excel(name = "接收用户ID")
    private Long userId;

    private User user;
    private String loginName;
}
