package qqzsh.top.preparation.project.content.message.controller;

import java.util.LinkedList;
import java.util.List;

import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Log;
import qqzsh.top.preparation.framework.aspectj.lang.enums.BusinessType;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.content.message.service.IMessageService;

import javax.servlet.http.HttpSession;

/**
 * 消息Controller
 * 
 * @author zsh
 * @date 2020-01-02
 */
@Controller
@RequestMapping("/content/message")
public class MessageController extends BaseController
{
    private String prefix = "content/message";

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserService userService;

    @RequiresPermissions("content:message:view")
    @GetMapping()
    public String message(HttpSession session) {
        //将个人消息未读全部转为已读
        User sysUser = ShiroUtils.getSysUser();
        messageService.updateState(sysUser.getUserId());
        //设置未读条数为0
        sysUser.setMessageCount(0);
        //更新session用户
        session.setAttribute("currentUser", sysUser);
        return prefix + "/message";
    }

    /**
     * 查询消息列表
     */
    @RequiresPermissions("content:message:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Message message) {
        List<Message> list = new LinkedList<>();
        startPage();
        // 除管理员外，个人只加载自己的消息
        User sysUser = ShiroUtils.getSysUser();
        // 普通会员
        if (ShiroUtils.isOrdinary(sysUser)){
            list = messageService.selectJoint(message.getContent(),sysUser.getLoginName(),
                    String.valueOf(message.getParams().get("beginPublishDate")),
                    String.valueOf(message.getParams().get("endPublishDate")));
        }else {
            // 管理员
            list = messageService.selectJoint(message.getContent(),message.getLoginName(),
                    String.valueOf(message.getParams().get("beginPublishDate")),
                    String.valueOf(message.getParams().get("endPublishDate")));
        }
        list.forEach(message1 -> {
            message1.setUser(userService.selectUserById(message1.getUserId()));
        });
        return getDataTable(list);
    }

    /**
     * 导出消息列表
     */
    @RequiresPermissions("content:message:export")
    @Log(title = "消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Message message) {
        List<Message> list = messageService.selectMessageList(message);
        ExcelUtil<Message> util = new ExcelUtil<Message>(Message.class);
        return util.exportExcel(list, "message");
    }

    /**
     * 新增消息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存消息
     */
    @RequiresPermissions("content:message:add")
    @Log(title = "消息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Message message)
    {
        return toAjax(messageService.insertMessage(message));
    }

    /**
     * 重发消息
     */
    @RequiresPermissions("content:message:resent")
    @Log(title = "消息", businessType = BusinessType.INSERT)
    @PostMapping("/resent")
    @ResponseBody
    public AjaxResult resent(Message message){
        Message message1 = messageService.selectMessageById(message.getId());
        message1.setSee(0);
        return toAjax(messageService.updateMessage(message1));
    }

    /**
     * 修改消息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Message message = messageService.selectMessageById(id);
        mmap.put("message", message);
        return prefix + "/edit";
    }

    /**
     * 修改保存消息
     */
    @RequiresPermissions("content:message:edit")
    @Log(title = "消息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Message message)
    {
        return toAjax(messageService.updateMessage(message));
    }

    /**
     * 删除消息
     */
    @RequiresPermissions("content:message:remove")
    @Log(title = "消息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(messageService.deleteMessageByIds(ids));
    }
}
