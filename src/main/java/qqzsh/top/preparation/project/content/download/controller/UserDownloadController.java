package qqzsh.top.preparation.project.content.download.controller;

import java.util.LinkedList;
import java.util.List;

import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
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
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.content.article.service.IArticleService;

/**
 * 用户已下载Controller
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Controller
@RequestMapping("/content/download")
public class UserDownloadController extends BaseController
{
    private String prefix = "content/download";

    @Autowired
    private IUserDownloadService userDownloadService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;

    @RequiresPermissions("content:download:view")
    @GetMapping()
    public String download()
    {
        return prefix + "/download";
    }

    /**
     * 查询用户已下载列表
     */
    @RequiresPermissions("content:download:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserDownload userDownload) {
        startPage();
        List<UserDownload> list = new LinkedList<>();
        // 判断下载资源名和登录用户名是否为空
        User sysUser = ShiroUtils.getSysUser();
        // 普通会员
        if (ShiroUtils.isOrdinary(sysUser)){
            list = userDownloadService.selectJoint(userDownload.getArticleName(),sysUser.getLoginName(),
                    String.valueOf(userDownload.getParams().get("beginDownloadDate")),
                    String.valueOf(userDownload.getParams().get("endDownloadDate")),null);
        }else {
            Long deptId = null;
            // 判断是否是高校管理员
            if (ShiroUtils.isCollegeAdmin(sysUser)){
                deptId = sysUser.getDeptId();
            }
            // 管理员
            list = userDownloadService.selectJoint(userDownload.getArticleName(),userDownload.getLoginName(),
                    String.valueOf(userDownload.getParams().get("beginDownloadDate")),
                    String.valueOf(userDownload.getParams().get("endDownloadDate")),deptId);
        }
        list.forEach(userDownload1 -> {
            userDownload1.setUser(userService.selectUserById(userDownload1.getDownloadUserId()));
            userDownload1.setArticle(articleService.selectArticleById(userDownload1.getDownloadArticleId()));
        });
        return getDataTable(list);
    }

    /**
     * 导出用户已下载列表
     */
    @RequiresPermissions("content:download:export")
    @Log(title = "用户已下载", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(UserDownload userDownload)
    {
        List<UserDownload> list = userDownloadService.selectUserDownloadList(userDownload);
        ExcelUtil<UserDownload> util = new ExcelUtil<UserDownload>(UserDownload.class);
        return util.exportExcel(list, "download");
    }

    /**
     * 新增用户已下载
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存用户已下载
     */
    @RequiresPermissions("content:download:add")
    @Log(title = "用户已下载", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UserDownload userDownload)
    {
        return toAjax(userDownloadService.insertUserDownload(userDownload));
    }

    /**
     * 修改用户已下载
     */
    @GetMapping("/edit/{downloadId}")
    public String edit(@PathVariable("downloadId") Long downloadId, ModelMap mmap)
    {
        UserDownload userDownload = userDownloadService.selectUserDownloadById(downloadId);
        mmap.put("userDownload", userDownload);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户已下载
     */
    @RequiresPermissions("content:download:edit")
    @Log(title = "用户已下载", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(UserDownload userDownload)
    {
        return toAjax(userDownloadService.updateUserDownload(userDownload));
    }

    /**
     * 删除用户已下载
     */
    @RequiresPermissions("content:download:remove")
    @Log(title = "用户已下载", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(userDownloadService.deleteUserDownloadByIds(ids));
    }
}
