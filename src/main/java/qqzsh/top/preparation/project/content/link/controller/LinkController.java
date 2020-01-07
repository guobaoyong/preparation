package qqzsh.top.preparation.project.content.link.controller;

import java.util.List;

import qqzsh.top.preparation.common.utils.RedisUtil;
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
import qqzsh.top.preparation.project.content.link.domain.Link;
import qqzsh.top.preparation.project.content.link.service.ILinkService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Log;
import qqzsh.top.preparation.framework.aspectj.lang.enums.BusinessType;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.content.link.domain.Link;
import qqzsh.top.preparation.project.content.link.service.ILinkService;

/**
 * 友链Controller
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Controller
@RequestMapping("/content/link")
public class LinkController extends BaseController
{
    private String prefix = "content/link";

    @Autowired
    private ILinkService linkService;

    @RequiresPermissions("content:link:view")
    @GetMapping()
    public String link()
    {
        return prefix + "/link";
    }

    /**
     * 查询友链列表
     */
    @RequiresPermissions("content:link:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Link link)
    {
        startPage();
        List<Link> list = linkService.selectLinkList(link);
        return getDataTable(list);
    }

    /**
     * 导出友链列表
     */
    @RequiresPermissions("content:link:export")
    @Log(title = "友链", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Link link)
    {
        List<Link> list = linkService.selectLinkList(link);
        ExcelUtil<Link> util = new ExcelUtil<Link>(Link.class);
        return util.exportExcel(list, "link");
    }

    /**
     * 新增友链
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存友链
     */
    @RequiresPermissions("content:link:add")
    @Log(title = "友链", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Link link)
    {
        return toAjax(linkService.insertLink(link));
    }

    /**
     * 修改友链
     */
    @GetMapping("/edit/{linkId}")
    public String edit(@PathVariable("linkId") Long linkId, ModelMap mmap)
    {
        Link link = linkService.selectLinkById(linkId);
        mmap.put("link", link);
        return prefix + "/edit";
    }

    /**
     * 修改保存友链
     */
    @RequiresPermissions("content:link:edit")
    @Log(title = "友链", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Link link)
    {
        return toAjax(linkService.updateLink(link));
    }

    /**
     * 删除友链
     */
    @RequiresPermissions("content:link:remove")
    @Log(title = "友链", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(linkService.deleteLinkByIds(ids));
    }



}
