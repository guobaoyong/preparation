package qqzsh.top.preparation.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 16:04
 * @Description 订单
 */
@Entity
@Table(name="t_order")
public class Order implements Serializable {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    //订单号
    private String orderNo;

    //支付宝订单号
    private String aliNo;

    // 用户ID
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    // 金额
    private Double money;

    // 订单详情
    private String detail;

    //订单状态
    private String status;

    //下单时间
    private Date createTime;

    //支付类型 支付宝、微信
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAliNo() {
        return aliNo;
    }

    public void setAliNo(String aliNo) {
        this.aliNo = aliNo;
    }
}
