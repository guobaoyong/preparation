package qqzsh.top.preparation.project.content.change.controller;

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
import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Log;
import qqzsh.top.preparation.framework.aspectj.lang.enums.BusinessType;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.change.domain.PointChange;
import qqzsh.top.preparation.project.content.change.service.IPointChangeService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import javax.servlet.http.HttpSession;

/**
 * 积分变更记录Controller
 * 
 * @author zsh
 * @date 2020-01-12
 */
@Controller
@RequestMapping("/content/change")
public class PointChangeController extends BaseController
{
    private String prefix = "content/change";

    @Autowired
    private IPointChangeService pointChangeService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IUserService userService;

    @RequiresPermissions("content:change:view")
    @GetMapping()
    public String change()
    {
        return prefix + "/change";
    }

    /**
     * 查询积分变更记录列表
     */
    @RequiresPermissions("content:change:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PointChange pointChange) {
        User sysUser = ShiroUtils.getSysUser();
        // 普通用户
        if (ShiroUtils.isOrdinary(sysUser)){
            pointChange.setPointUserId(sysUser.getUserId());
        }
        // 院系管理员
        if (ShiroUtils.isCollegeAdmin(sysUser)){
            pointChange.setDeptId(sysUser.getDeptId());
        }
        startPage();
        List<PointChange> list = pointChangeService.selectPointChangeList(pointChange);
        return getDataTable(list);
    }

    /**
     * 导出积分变更记录列表
     */
    @RequiresPermissions("content:change:export")
    @Log(title = "积分变更记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PointChange pointChange) {
        User sysUser = ShiroUtils.getSysUser();
        if (ShiroUtils.isOrdinary(sysUser)){
            pointChange.setPointUserId(sysUser.getUserId());
        }
        List<PointChange> list = pointChangeService.selectPointChangeList(pointChange);
        ExcelUtil<PointChange> util = new ExcelUtil<PointChange>(PointChange.class);
        return util.exportExcel(list, "change");
    }

    /**
     * 新增积分变更记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存积分变更记录
     */
    @RequiresPermissions("content:change:add")
    @Log(title = "积分变更记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PointChange pointChange) {
        User sysUser = ShiroUtils.getSysUser();
        // 判断是否有待处理的提现记录
        PointChange p = new PointChange();
        p.setPointUserId(sysUser.getUserId());
        p.setPointStatus(1);
        if (pointChangeService.selectPointChangeList(p).size() != 0){
            return AjaxResult.error("您的账户存在未处理的提现记录，请耐心等待管理员审核！");
        }
        // 判断积分是否足够
        if (pointChange.getPointChange() > sysUser.getPoint()){
            return AjaxResult.error("积分不足,您账户的可用积分为："+sysUser.getPoint());
        }
        // 组装pointChange对象
        // 兑换比例
        String key = configService.getKey("point.proportion");
        pointChange.setPointContent("申请积分提现，本次申请兑换："+pointChange.getPointChange()+
                "积分，兑换比例为"+key+",预计兑换金额："+
                (double)pointChange.getPointChange()/Double.parseDouble(key.split(":")[0])+"元");
        pointChange.setPointUserId(sysUser.getUserId());
        pointChange.setPointStatus(1);
        pointChange.setPointLoginName(sysUser.getLoginName());
        pointChange.setPointCreateTime(DateUtils.getNowDate());
        pointChange.setPointSymbol("-");
        return toAjax(pointChangeService.insertPointChange(pointChange));
    }

    /**
     * 修改积分变更记录
     */
    @GetMapping("/edit/{pointId}")
    public String edit(@PathVariable("pointId") Long pointId, ModelMap mmap)
    {
        PointChange pointChange = pointChangeService.selectPointChangeById(pointId);
        User user = userService.selectUserById(pointChange.getPointUserId());
        pointChange.setPointFront(Long.valueOf(user.getPoint()));
        if ("+".equals(pointChange.getPointSymbol())){
            pointChange.setPointEnd(Long.valueOf(user.getPoint()) + pointChange.getPointChange());
        }else if ("-".equals(pointChange.getPointSymbol())){
            pointChange.setPointEnd(Long.valueOf(user.getPoint()) - pointChange.getPointChange());
        }
        mmap.put("pointChange", pointChange);
        return prefix + "/edit";
    }

    /**
     * 修改保存积分变更记录
     */
    @RequiresPermissions("content:change:edit")
    @Log(title = "积分变更记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PointChange pointChange,
                               HttpSession session) {
        User user = userService.selectUserById(pointChange.getPointUserId());
        pointChange.setPointFront(Long.valueOf(user.getPoint()));
        if ("+".equals(pointChange.getPointSymbol())){
            pointChange.setPointEnd(Long.valueOf(user.getPoint()) + pointChange.getPointChange());
        }else if ("-".equals(pointChange.getPointSymbol())){
            pointChange.setPointEnd(Long.valueOf(user.getPoint()) - pointChange.getPointChange());
        }
        if (pointChange.getPointEnd() < 0){
            return AjaxResult.error("积分已不足以兑换");
        }
        // 状态是否变为已处理
        PointChange old = pointChangeService.selectPointChangeById(pointChange.getPointId());
        if (old.getPointStatus() == 1){
            // 初次审核通过
            if (pointChange.getPointStatus() == 2){
                user.setPoint(Integer.parseInt(String.valueOf(pointChange.getPointEnd())));
                userService.updateUserInfo(user);
            }
        }else {
            return AjaxResult.error("此申请已进行过审核！");
        }
        if (ShiroUtils.getSysUser().getUserId().equals(user.getUserId())){
            session.setAttribute("currentUser",user);
        }
        return toAjax(pointChangeService.updatePointChange(pointChange));
    }

    /**
     * 删除积分变更记录
     */
    @RequiresPermissions("content:change:remove")
    @Log(title = "积分变更记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(pointChangeService.deletePointChangeByIds(ids));
    }
}
