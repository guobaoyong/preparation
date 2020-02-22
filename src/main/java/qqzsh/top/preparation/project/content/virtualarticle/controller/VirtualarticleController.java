package qqzsh.top.preparation.project.content.virtualarticle.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import me.zhyd.hunter.config.HunterConfig;
import me.zhyd.hunter.config.HunterConfigContext;
import me.zhyd.hunter.config.platform.Platform;
import me.zhyd.hunter.entity.VirtualArticle;
import me.zhyd.hunter.enums.ExitWayEnum;
import me.zhyd.hunter.processor.BlogHunterProcessor;
import me.zhyd.hunter.processor.HunterProcessor;
import org.apache.commons.lang3.StringUtils;
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
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.project.content.virtualarticle.domain.Virtualarticle;
import qqzsh.top.preparation.project.content.virtualarticle.service.IVirtualarticleService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.system.dict.domain.DictData;
import qqzsh.top.preparation.project.system.dict.service.IDictDataService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

/**
 * 爬虫记录Controller
 * 
 * @author zsh
 * @date 2020-02-22
 */
@Controller
@RequestMapping("/content/virtualarticle")
public class VirtualarticleController extends BaseController
{
    private String prefix = "content/virtualarticle";

    @Autowired
    private IVirtualarticleService virtualarticleService;

    @RequiresPermissions("content:virtualarticle:view")
    @GetMapping()
    public String virtualarticle()
    {
        return prefix + "/virtualarticle";
    }

    /**
     * 查询爬虫记录列表
     */
    @RequiresPermissions("content:virtualarticle:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Virtualarticle virtualarticle)
    {
        startPage();
        List<Virtualarticle> list = virtualarticleService.selectVirtualarticleList(virtualarticle);
        return getDataTable(list);
    }

    /**
     * 导出爬虫记录列表
     */
    @RequiresPermissions("content:virtualarticle:export")
    @Log(title = "爬虫记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Virtualarticle virtualarticle)
    {
        List<Virtualarticle> list = virtualarticleService.selectVirtualarticleList(virtualarticle);
        ExcelUtil<Virtualarticle> util = new ExcelUtil<Virtualarticle>(Virtualarticle.class);
        return util.exportExcel(list, "virtualarticle");
    }

    /**
     * 新增爬虫记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存爬虫记录
     */
    @RequiresPermissions("content:virtualarticle:add")
    @Log(title = "爬虫记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Virtualarticle virtualarticle)
    {
        return toAjax(virtualarticleService.insertVirtualarticle(virtualarticle));
    }

    /**
     * 修改爬虫记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Virtualarticle virtualarticle = virtualarticleService.selectVirtualarticleById(id);
        mmap.put("virtualarticle", virtualarticle);
        return prefix + "/edit";
    }

    /**
     * 修改保存爬虫记录
     */
    @RequiresPermissions("content:virtualarticle:edit")
    @Log(title = "爬虫记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Virtualarticle virtualarticle)
    {
        return toAjax(virtualarticleService.updateVirtualarticle(virtualarticle));
    }

    /**
     * 删除爬虫记录
     */
    @RequiresPermissions("content:virtualarticle:remove")
    @Log(title = "爬虫记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(virtualarticleService.deleteVirtualarticleByIds(ids));
    }

}
