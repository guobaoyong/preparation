package qqzsh.top.preparation.project.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.util.StringUtil;
import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.config.AliPayConfig;
import qqzsh.top.preparation.framework.config.PreparationConfig;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.order.domain.Order;
import qqzsh.top.preparation.project.content.order.service.IOrderService;
import qqzsh.top.preparation.project.system.menu.domain.Menu;
import qqzsh.top.preparation.project.system.menu.service.IMenuService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.order.domain.Order;
import qqzsh.top.preparation.project.content.order.service.IOrderService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-01-02 16:12
 * @description 支付宝控制器
 */
@Controller
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @Resource
    private RedisUtil redisNum;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private PreparationConfig preparationConfig;

    /**
     * 支付请求
     */
    @RequestMapping("/pay")
    public void pay(String time, HttpSession session, HttpServletResponse response) {
        User user = ShiroUtils.getSysUser();
        // 生成订单号
        String orderNo = null;
        try {
            orderNo = DateUtils.getCurrentDateStr();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 支付总金额
        String totalAmount = "";
        // 订单名称
        String subject = "";
        // 商品描述
        String body = "";
        switch (Integer.parseInt(time)) {
            case 10:
                totalAmount = "10";
                subject = "购买VIP";
                body = "一个月-10元";
                break;
            case 30:
                totalAmount = "30";
                subject = "购买VIP";
                body = "三个月-30元";
                break;
            case 60:
                totalAmount = "60";
                subject = "购买VIP";
                body = "半年-60元";
                break;
            case 120:
                totalAmount = "120";
                subject = "购买VIP";
                body = "一年-120元";
                break;
            case 360:
                totalAmount = "360";
                subject = "购买VIP";
                body = "三年-360元";
                break;
            case 888:
                totalAmount = "888";
                subject = "购买VIP";
                body = "永久-888元";
                break;
            default:
                totalAmount = "0.01";
                subject = "出错金额";
                body = "请哥吃顿饭";
        }

        Order order = new Order();
        //用户Id
        order.setUserId(user.getUserId());
        //订单号
        order.setOrderNo(orderNo);
        //订单详情
        order.setDetail(subject + ";" + body);
        //支付金额
        order.setMoney(Double.parseDouble(totalAmount));
        //订单状态
        order.setStatus("未支付");
        //下单时间
        order.setCreateTime(new Date());
        //支付类型
        order.setType("支付宝");

        // 保存订单信息
        orderService.insertOrder(order);

        String form = ""; // 生成支付表单

        AliPayConfig aliPayConfig = buildParams();

        // 封装请求客户端
        AlipayClient client = new DefaultAlipayClient(aliPayConfig.getUrl(), aliPayConfig.getAppid(), aliPayConfig.getRsa_private_key(), aliPayConfig.getFormat(), aliPayConfig.getCharset(), aliPayConfig.getAlipay_public_key(), aliPayConfig.getSigntype());

        // 支付请求
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(aliPayConfig.getReturn_url());
        alipayRequest.setNotifyUrl(aliPayConfig.getNotify_url());
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setProductCode("FAST_INSTANT_TRADE_PAY"); // 设置销售产品码
        model.setOutTradeNo(orderNo); // 设置订单号
        model.setSubject(subject); // 订单名称
        model.setTotalAmount(totalAmount); // 支付总金额
        model.setBody(body); // 设置商品描述
        alipayRequest.setBizModel(model);

        try {
            form = client.pageExecute(alipayRequest).getBody(); // 生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html;charset=" + aliPayConfig.getCharset());
        try {
            response.getWriter().write(form); // 直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝服务器同步通知页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/success")
    public ModelAndView returnUrl(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "同步通知地址_"+configService.getKey("site.name"));
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
            System.out.println("name:" + name + ",valueStr:" + valueStr);
        }

        AliPayConfig aliPayConfig = buildParams();

        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipay_public_key(), aliPayConfig.getCharset(), aliPayConfig.getSigntype()); //调用SDK验证签名

        if (signVerified) {
            // 取身份信息
            User user = ShiroUtils.getSysUser();
            // 格式化VIP时间
            user.setVipTimeStr(DateUtils.formatDate(user.getVipTime()));
            // 根据用户id取出菜单
            List<Menu> menus = menuService.selectMenusByUser(user);
            mav.addObject("menus", menus);
            mav.addObject("user", user);
            mav.addObject("copyrightYear", preparationConfig.getCopyrightYear());
            mav.addObject("demoEnabled", preparationConfig.isDemoEnabled());
            request.getSession().setAttribute("currentUser",user);
            mav.setViewName("redirect:/index");
        } else {
            mav.setViewName("redirect:/");
        }
        return mav;
    }

    /**
     * 支付宝服务器异步通知
     *
     * @param request
     * @return
     */
    @PostMapping("/sy")
    @ResponseBody
    public void notifyUrl(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
            System.out.println("name:" + name + ",valueStr:" + valueStr);
        }
        AliPayConfig aliPayConfig = buildParams();

        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipay_public_key(), aliPayConfig.getCharset(), aliPayConfig.getSigntype()); //调用SDK验证签名
        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");
        //交易状态
        String trade_status = request.getParameter("trade_status");
        //total_amount
        String total_amount = request.getParameter("total_amount");
        //阿里订单号
        String trade_no = request.getParameter("trade_no");

        // 验证成功 更新订单信息
        if (signVerified) {
            if (trade_status.equals("TRADE_SUCCESS")) {
                if (StringUtil.isNotEmpty(out_trade_no)
                        && StringUtil.isNotEmpty(trade_no)) {
                    List<Order> orders = orderService.selectOrderList(Order.builder().orderNo(out_trade_no).build());// 获取订单
                    if (orders.size() != 0) {
                        Order order = orders.get(0);
                        // 设置支付状态已经支付
                        order.setStatus("已支付");
                        //设置支付宝订单号
                        order.setAliNo(trade_no);
                        orderService.updateOrder(order);
                        User byId = userService.selectUserById(order.getUserId());
                        Calendar rightNow = Calendar.getInstance();
                        Date date = byId.getVipTime() == null ? new Date() : byId.getVipTime();
                        rightNow.setTime(date);
                        //更新用户的VIP过期时间
                        switch (total_amount) {
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
                                byId.setVipTime(null);
                        }
                        //更新时间
                        byId.setVipTime(rightNow.getTime());
                        userService.updateUserInfo(byId);
                    }
                }
            }
            // 取身份信息
            User user = ShiroUtils.getSysUser();
            // 格式化VIP时间
            user.setVipTimeStr(DateUtils.formatDate(user.getVipTime()));
            session.setAttribute("currentUser",user);
            response.getWriter().write("success");
        } else {
            //验证失败
            response.getWriter().write("fail");
        }
        response.getWriter().flush();
        response.getWriter().close();
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

