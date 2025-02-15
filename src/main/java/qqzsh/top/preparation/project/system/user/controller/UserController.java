package qqzsh.top.preparation.project.system.user.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.project.content.change.domain.PointChange;
import qqzsh.top.preparation.project.content.change.service.IPointChangeService;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import qqzsh.top.preparation.common.constant.UserConstants;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Log;
import qqzsh.top.preparation.framework.aspectj.lang.enums.BusinessType;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.post.service.IPostService;
import qqzsh.top.preparation.project.system.role.domain.Role;
import qqzsh.top.preparation.project.system.role.service.IRoleService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;
import qqzsh.top.preparation.common.constant.UserConstants;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Log;
import qqzsh.top.preparation.framework.aspectj.lang.enums.BusinessType;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.system.user.domain.User;

/**
 * 用户信息
 * 
 * @author zsh
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    private String prefix = "system/user";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPostService postService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IPointChangeService pointChangeService;

    @Autowired
    private IDeptService deptService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(User user) {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(User user)
    {
        List<User> list = userService.selectUserList(user);
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        List<User> userList = util.importExcel(file.getInputStream());
        String message = userService.importUser(userList, updateSupport);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:user:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        // 院系管理员 只显示院系管理员和普通用户
        List<Role> roles = roleService.selectRoleAll();
        List<Role> result = new ArrayList<>();
        roles.forEach(role -> {
            if (!role.getRoleKey().equalsIgnoreCase("admin")){
                result.add(role);
            }
        });
        mmap.put("roles", result);
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated User user) {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        // 院系管理员 只显示院系管理员和普通用户
        List<Role> roles = roleService.selectRolesByUserId(userId);
        List<Role> result = new ArrayList<>();
        roles.forEach(role -> {
            if (!role.getRoleKey().equalsIgnoreCase("admin")){
                result.add(role);
            }
        });
        mmap.put("roles", result);
        mmap.put("posts", postService.selectPostsByUserId(userId));
        return prefix + "/edit";
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated User user)
    {
        userService.checkUserAllowed(user);
        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return error("修改用户'" + user.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return error("修改用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.updateUser(user));
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:addpoint")
    @Log(title = "增加积分", businessType = BusinessType.UPDATE)
    @GetMapping("/addPoint/{userId}")
    public String addPoint(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/addPoints";
    }

    @RequiresPermissions("system:user:addpoint")
    @Log(title = "增加积分", businessType = BusinessType.UPDATE)
    @PostMapping("/addPoints")
    @ResponseBody
    public AjaxResult addPointSave(User user) {
        User byId = userService.selectUserById(user.getUserId());
        //积分变更记录
        PointChange pointChange = new PointChange();
        pointChange.setPointContent("管理员【"+ ShiroUtils.getSysUser().getLoginName()+"】为【"+byId.getLoginName()+"】增加积分");
        pointChange.setPointFront(Long.parseLong(String.valueOf(byId.getPoint())));
        pointChange.setPointEnd(Long.parseLong(String.valueOf(byId.getPoint() + user.getPoint())));
        pointChange.setPointChange(Long.parseLong(String.valueOf(user.getPoint())));
        pointChange.setPointUserId(byId.getUserId());
        pointChange.setPointStatus(2);
        pointChange.setPointLoginName(byId.getLoginName());
        pointChange.setPointCreateTime(DateUtils.getNowDate());
        pointChange.setPointUpdateTime(DateUtils.getNowDate());
        pointChange.setPointSymbol("+");
        pointChangeService.insertPointChange(pointChange);

        return toAjax(userService.updateUserInfo(user));
    }

    @RequiresPermissions("system:user:sentmessage")
    @Log(title = "发送消息", businessType = BusinessType.INSERT)
    @GetMapping("/sentMessage/{userId}")
    public String sentMessage(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/sentMessage";
    }

    @RequiresPermissions("system:user:sentmessage")
    @Log(title = "发送消息", businessType = BusinessType.INSERT)
    @PostMapping("/sentMessage")
    @ResponseBody
    public AjaxResult sentMessage(Message message) {
        message.setSee(0);
        message.setPublishDate(new Date());
        return toAjax(messageService.insertMessage(message));
    }


    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(User user)
    {
        userService.checkUserAllowed(user);
        if (userService.resetUserPwd(user) > 0)
        {
            if (ShiroUtils.getUserId() == user.getUserId())
            {
                setSysUser(userService.selectUserById(user.getUserId()));
            }
            return success();
        }
        return error();
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(userService.deleteUserByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(User user)
    {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(User user)
    {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(User user)
    {
        return userService.checkEmailUnique(user);
    }

    /**
     * 用户状态修改
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(User user)
    {
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }
}