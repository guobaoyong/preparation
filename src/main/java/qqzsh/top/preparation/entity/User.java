package qqzsh.top.preparation.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 15:39
 * @description 用户实体
 */
@Entity
@Table(name="t_user")
public class User implements Serializable {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    // 用户名
    @NotEmpty(message="请输入用户名！")
    @Column(length=100)
    private String userName;

    // 密码
    @NotEmpty(message="请输入密码！")
    @Length(min = 6, message ="密码长度不能小于6位！")
    @Column(length=100)
    private String password;

    // 验证邮箱地址
    @Email(message="邮箱地址格式有误！")
    @NotEmpty(message="请输入邮箱地址！")
    @Column(length=100)
    private String email;

    // 用户头像
    @Column(length=100)
    private String imageName;

    // 用户积分
    private Integer points=0;

    // 是否是VIP
    private boolean isVip=false;

    //vip到期时间
    private Date endtime;

    // 是否被封禁
    private boolean isOff=false;

    // 角色名称 会员  管理员 两种 默认 会员
    private String roleName="会员";

    // 注册日期
    private Date registerDate;

    // 未查看的消息记录数
    @Transient
    private Integer messageCount;

    // 是否签到
    private boolean isSign=false;

    // 签到时间
    private Date signTime;

    // 签到排序
    private Integer signSort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean isVip) {
        this.isVip = isVip;
    }

    public boolean isOff() {
        return isOff;
    }

    public void setOff(boolean isOff) {
        this.isOff = isOff;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @JsonSerialize(using=CustomDateSerializer.class)
    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Integer getSignSort() {
        return signSort;
    }

    public void setSignSort(Integer signSort) {
        this.signSort = signSort;
    }

    @JsonSerialize(using=CustomDateSerializer.class)
    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", imageName='" + imageName + '\'' +
                ", points=" + points +
                ", isVip=" + isVip +
                ", endtime=" + endtime +
                ", isOff=" + isOff +
                ", roleName='" + roleName + '\'' +
                ", registerDate=" + registerDate +
                ", messageCount=" + messageCount +
                ", isSign=" + isSign +
                ", signTime=" + signTime +
                ", signSort=" + signSort +
                '}';
    }
}
