package qqzsh.top.preparation.project.content.type.controller;

import java.util.List;
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
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;

/**
 * 资源类别Controller
 * 
 * @author zsh
 * @date 2019-12-30
 */
@Controller
@RequestMapping("/content/type")
public class ArcTypeController extends BaseController
{
    private String prefix = "content/type";

    @Autowired
    private IArcTypeService arcTypeService;

    @RequiresPermissions("content:type:view")
    @GetMapping()
    public String type()
    {
        return prefix + "/type";
    }

    /**
     * 查询资源类别列表
     */
    @RequiresPermissions("content:type:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ArcType arcType)
    {
        startPage();
        List<ArcType> list = arcTypeService.selectArcTypeList(arcType);
        return getDataTable(list);
    }

    /**
     * 导出资源类别列表
     */
    @RequiresPermissions("content:type:export")
    @Log(title = "资源类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ArcType arcType)
    {
        List<ArcType> list = arcTypeService.selectArcTypeList(arcType);
        ExcelUtil<ArcType> util = new ExcelUtil<ArcType>(ArcType.class);
        return util.exportExcel(list, "type");
    }

    /**
     * 新增资源类别
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存资源类别
     */
    @RequiresPermissions("content:type:add")
    @Log(title = "资源类别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ArcType arcType)
    {
        return toAjax(arcTypeService.insertArcType(arcType));
    }

    /**
     * 修改资源类别
     */
    @GetMapping("/edit/{srcTypeId}")
    public String edit(@PathVariable("srcTypeId") Long srcTypeId, ModelMap mmap)
    {
        ArcType arcType = arcTypeService.selectArcTypeById(srcTypeId);
        mmap.put("arcType", arcType);
        return prefix + "/edit";
    }

    /**
     * 修改保存资源类别
     */
    @RequiresPermissions("content:type:edit")
    @Log(title = "资源类别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ArcType arcType)
    {
        return toAjax(arcTypeService.updateArcType(arcType));
    }

    /**
     * 删除资源类别
     */
    @RequiresPermissions("content:type:remove")
    @Log(title = "资源类别", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(arcTypeService.deleteArcTypeByIds(ids));
    }
}
