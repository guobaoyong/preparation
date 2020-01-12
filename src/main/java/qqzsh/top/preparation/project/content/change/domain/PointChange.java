package qqzsh.top.preparation.project.content.change.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 积分变更记录对象 sys_point_change
 * 
 * @author zsh
 * @date 2020-01-12
 */
@Data
public class PointChange extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long pointId;

    /** 积分变更记录 */
    @Excel(name = "积分变更记录")
    private String pointContent;

    /** 变更前积分 */
    @Excel(name = "变更前积分")
    private Long pointFront;

    /** 变更后积分 */
    @Excel(name = "变更后积分")
    private Long pointEnd;

    /** 变更积分 */
    @Excel(name = "变更积分")
    private Long pointChange;

    /** 变更用户 */
    @Excel(name = "变更用户")
    private Long pointUserId;

    /** 变更状态 */
    @Excel(name = "变更状态")
    private Integer pointStatus;

    /** 变更用户账号 */
    @Excel(name = "变更用户账号")
    private String pointLoginName;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pointCreateTime;

    /** 更新时间 */
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pointUpdateTime;

    /** 提现方式 */
    @Excel(name = "提现方式")
    private Integer pointType;

    /** 提现账号 */
    @Excel(name = "提现账号")
    private String pointAccount;

    /** 驳回理由 */
    @Excel(name = "驳回理由")
    private String pointReason;

    /** 正负号 */
    @Excel(name = "符号")
    private String pointSymbol;
}
