package qqzsh.top.preparation.project.content.order.domain;

import qqzsh.top.preparation.project.system.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 订单对象 sys_order
 * 
 * @author zsh
 * @date 2020-01-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long id;

    /** 细节 */
    @Excel(name = "细节")
    private String detail;

    /** 金额 */
    @Excel(name = "金额")
    private Double money;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 付款方式 */
    @Excel(name = "付款方式")
    private String type;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 支付宝商品订单号 */
    @Excel(name = "支付宝商品订单号")
    private String aliNo;

    /** 支付宝付款订单号 */
    @Excel(name = "支付宝付款订单号")
    private String orderNo;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private Date createTime;

    private User user;
    private String loginName;
}
