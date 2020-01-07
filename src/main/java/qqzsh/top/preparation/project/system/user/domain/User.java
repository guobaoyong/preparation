package qqzsh.top.preparation.project.system.user.domain;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;

import qqzsh.top.preparation.common.utils.DateUtils;
import lombok.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel.ColumnType;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel.Type;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excels;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import qqzsh.top.preparation.project.system.dept.domain.Dept;
import qqzsh.top.preparation.project.system.role.domain.Role;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excels;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;

/**
 * 用户对象 sys_user
 * 
 * @author zsh
 */
@Data
@NoArgsConstructor
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", cellType = Excel.ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /** 部门ID */
    @Excel(name = "部门编号", type = Excel.Type.IMPORT)
    private Long deptId;

    /** 部门父ID */
    private Long parentId;

    /** 角色ID */
    private Long roleId;

    /** 登录名称 */
    @Excel(name = "登录名称")
    private String loginName;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 盐加密 */
    private String salt;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登陆IP */
    @Excel(name = "最后登陆IP", type = Excel.Type.EXPORT)
    private String loginIp;

    /** 最后登陆时间 */
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date loginDate;

    /** 最后登陆时间 */
    @Excel(name = "VIP到期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date vipTime;

    private String vipTimeStr;

    /** 部门对象 */
    @Excels({
        @Excel(name = "部门名称", targetAttr = "deptName", type = Excel.Type.EXPORT),
        @Excel(name = "部门负责人", targetAttr = "leader", type = Excel.Type.EXPORT)
    })
    private Dept dept;

    private List<Role> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;

    /** 积分 */
    private Integer point;

    /** 是否签到 */
    private boolean sign;

    /** 签到时间 */
    private Date signTime;

    /** 签到排序 */
    private Integer signSort;

    /** 未读消息的数量 */
    private Integer messageCount;

    public User(Long userId) {
        this.userId = userId;
    }

    @NotBlank(message = "登录账号不能为空")
    @Size(min = 0, max = 30, message = "登录账号长度不能超过30个字符")
    public String getLoginName() {
        return loginName;
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getUserName() {
        return userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * 生成随机盐
     */
    public void randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        setSalt(hex);
    }

    public Dept getDept() {
        if (dept == null) {
            dept = new Dept();
        }
        return dept;
    }
}
