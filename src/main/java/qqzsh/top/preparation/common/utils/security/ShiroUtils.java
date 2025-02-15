package qqzsh.top.preparation.common.utils.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.common.utils.bean.BeanUtils;
import qqzsh.top.preparation.framework.shiro.realm.UserRealm;
import qqzsh.top.preparation.project.system.role.domain.Role;
import qqzsh.top.preparation.project.system.user.domain.User;

import java.util.List;

/**
 * shiro 工具类
 * 
 * @author zsh
 */
public class ShiroUtils
{
    public static Subject getSubject()
    {
        return SecurityUtils.getSubject();
    }

    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout()
    {
        getSubject().logout();
    }

    public static User getSysUser() {
        User user = null;
        Object obj = getSubject().getPrincipal();
        if (StringUtils.isNotNull(obj)) {
            user = new User();
            BeanUtils.copyBeanProp(user, obj);
        }
        return user;
    }

    /**
     * 判断当前User是否是普通用户
     * @param user
     * @return
     */
    public static boolean isOrdinary(User user){
        List<Role> roles = user.getRoles();
        if (roles.size() != 0){
            return "common".equalsIgnoreCase(roles.get(0).getRoleKey());
        }else {
            return false;
        }
    }

    /**
     * 判断是否是高校管理员
     * @param user
     * @return
     */
    public static boolean isCollegeAdmin(User user){
        List<Role> roles = user.getRoles();
        if (roles.size() != 0){
            return "college_administrator".equalsIgnoreCase(roles.get(0).getRoleKey());
        }else {
            return false;
        }
    }

    /**
     * 判断当前用户是否是超级管理员
     * @param user
     * @return
     */
    public static boolean isAdmin(User user) {
        List<Role> roles = user.getRoles();
        if (roles.size() != 0){
            return "admin".equalsIgnoreCase(roles.get(0).getRoleKey());
        }else {
            return false;
        }
    }

    public static void setSysUser(User user)
    {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    public static void clearCachedAuthorizationInfo()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static Long getUserId()
    {
        return getSysUser().getUserId().longValue();
    }

    public static String getLoginName()
    {
        return getSysUser().getLoginName();
    }

    public static String getIp()
    {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId()
    {
        return String.valueOf(getSubject().getSession().getId());
    }
}
