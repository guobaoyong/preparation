package qqzsh.top.preparation.project.content.order.controller;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.config.AliPayConfig;
import qqzsh.top.preparation.framework.web.service.ConfigService;
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
import qqzsh.top.preparation.project.content.order.domain.Order;
import qqzsh.top.preparation.project.content.order.service.IOrderService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Log;
import qqzsh.top.preparation.framework.aspectj.lang.enums.BusinessType;
import qqzsh.top.preparation.framework.config.AliPayConfig;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.order.domain.Order;
import qqzsh.top.preparation.project.content.order.service.IOrderService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import javax.servlet.http.HttpSession;

/**
 * 订单Controller
 * 
 * @author zsh
 * @date 2020-01-02
 */
@Controller
@RequestMapping("/content/order")
public class OrderController extends BaseController
{
    private String prefix = "content/order";

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ConfigService configService;

    @RequiresPermissions("content:order:view")
    @GetMapping()
    public String order()
    {
        return prefix + "/order";
    }

    /**
     * 查询订单列表
     */
    @RequiresPermissions("content:order:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Order order) {
        User sysUser = ShiroUtils.getSysUser();
        //普通用户
        if (ShiroUtils.isOrdinary(sysUser)){
            order.setUserId(sysUser.getUserId());
        }
        //高校管理员
        if (ShiroUtils.isCollegeAdmin(sysUser)){
            order.setDeptId(sysUser.getDeptId());
        }
        startPage();
        List<Order> list = orderService.selectOrderList(order);
        list.forEach(order1 -> {
            order1.setUser(userService.selectUserById(order1.getUserId()));
        });
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @RequiresPermissions("content:order:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Order order)
    {
        List<Order> list = orderService.selectOrderList(order);
        ExcelUtil<Order> util = new ExcelUtil<Order>(Order.class);
        return util.exportExcel(list, "order");
    }

    /**
     * 新增订单
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存订单
     */
    @RequiresPermissions("content:order:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Order order)
    {
        return toAjax(orderService.insertOrder(order));
    }

    /**
     * 修改订单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Order order = orderService.selectOrderById(id);
        mmap.put("order", order);
        return prefix + "/edit";
    }

    /**
     * 修改保存订单
     */
    @RequiresPermissions("content:order:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Order order)
    {
        return toAjax(orderService.updateOrder(order));
    }

    /**
     * 删除订单
     */
    @RequiresPermissions("content:order:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(orderService.deleteOrderByIds(ids));
    }

    /**
     * 订单复议
     * @param id
     * @param session
     * @return
     */
    @RequiresPermissions("content:order:check")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PostMapping("/check")
    @ResponseBody
    public AjaxResult check(Long id, HttpSession session) {
        AliPayConfig aliPayConfig = buildParams();
        //根据id查询信息
        Order byId = orderService.selectOrderById(id);
        if (byId.getStatus().equals("已支付")) {
            return AjaxResult.error("此订单无异议");
        } else {
            //根据订单号查询支付状态
            String orderNo = byId.getOrderNo();
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.getUrl(), aliPayConfig.getAppid(), aliPayConfig.getRsa_private_key(), aliPayConfig.getFormat(), aliPayConfig.getCharset(), aliPayConfig.getAlipay_public_key(), aliPayConfig.getSigntype());
            //设置请求参数
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            AlipayTradePayModel model = new AlipayTradePayModel();
            // 设置订单号
            model.setOutTradeNo(orderNo);
            //请求
            alipayRequest.setBizModel(model);
            AlipayTradeQueryResponse result = null;
            try {
                result = alipayClient.execute(alipayRequest);
            } catch (AlipayApiException e) { }
            String tradeStatus = result.getTradeStatus();
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                byId.setStatus("已支付");
                byId.setAliNo(result.getTradeNo());
                orderService.updateOrder(byId);
                User user = userService.selectUserById(byId.getUserId());
                //根据金额去增加相应时间
                Calendar rightNow = Calendar.getInstance();
                Date date = user.getVipTime() == null ? new Date() : user.getVipTime();
                rightNow.setTime(date);
                //更新用户的VIP过期时间
                switch (result.getTotalAmount()) {
                    case "10.00":
                        //在原来时间基础上增加一个月
                        rightNow.add(Calendar.MONTH, 1);
                        break;
                    case "30.00":
                        //在原来时间基础上增加三个月
                        rightNow.add(Calendar.MONTH, 3);
                        break;
                    case "60.00":
                        //在原来时间基础上增加六个月
                        rightNow.add(Calendar.MONTH, 6);
                        break;
                    case "120.00":
                        //在原来时间基础上增加一年
                        rightNow.add(Calendar.YEAR, 1);
                        break;
                    case "360.00":
                        //在原来时间基础上增加三年
                        rightNow.add(Calendar.YEAR, 3);
                        break;
                    case "888.00":
                        //在原来时间基础上增加50年
                        rightNow.add(Calendar.YEAR, 50);
                        break;
                    default:
                }
                //更新时间
                user.setVipTime(rightNow.getTime());
                userService.updateUser(user);
                //更新session
                session.setAttribute("currentUser", user);
                return AjaxResult.success("复议成功！");
            } else {
                return AjaxResult.error("此订单无异议");
            }
        }
    }

    /**
     * 构建alipay参数实体
     * @return
     */
    private AliPayConfig buildParams(){
        Map<String,String> map = (Map<String,String>) JSONObject.parseObject(configService.getKey("alipay")).get("alipay");
        return AliPayConfig.builder()
                .appid(map.get("appid"))
                .rsa_private_key(map.get("rsa_private_key"))
                .notify_url(map.get("notify_url"))
                .return_url(map.get("return_url"))
                .url(map.get("url"))
                .charset(map.get("charset"))
                .format(map.get("format"))
                .alipay_public_key(map.get("alipay_public_key"))
                .signtype(map.get("signtype")).build();
    }

}
