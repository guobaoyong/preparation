package qqzsh.top.preparation.controller.user;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.config.AliPayConfig;
import qqzsh.top.preparation.entity.Order;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.OrderService;
import qqzsh.top.preparation.service.UserService;
import qqzsh.top.preparation.util.DateUtil;
import qqzsh.top.preparation.util.RedisUtil;
import qqzsh.top.preparation.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 19:50
 * @Description 支付宝控制器
 */
@Controller
@RequestMapping("/alipay")
public class AliPayController {

    @Resource
    private AliPayConfig aliPayConfig;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Resource
    private RedisUtil<Integer> redisNum;

    /**
     * 支付请求
     */
    @RequestMapping("/pay")
    public void pay(String time, HttpSession session, HttpServletResponse response) {
        User user = (User) session.getAttribute("currentUser");
        // 生成订单号
        String orderNo = null;
        try {
            orderNo = DateUtil.getCurrentDateStr();
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
        order.setUser(user);
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
        orderService.save(order);

        //更新索redis
        if (!redisNum.hasKey("orderNums")) {
            redisNum.set("orderNums", orderService.getTotal(null).intValue());
        } else {
            int num = (int) redisNum.get("orderNums");
            redisNum.del("orderNums");
            redisNum.set("orderNums", num + 1);
        }

        String form = ""; // 生成支付表单

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
        mav.addObject("title", "同步通知地址_校园学习资源共享云平台");
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
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

        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipay_public_key(), aliPayConfig.getCharset(), aliPayConfig.getSigntype()); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            mav.addObject("title", "支付成功");
            mav.setViewName("user/paySuccess");
        } else {
            mav.addObject("title", "支付失败");
            mav.setViewName("user/payError");
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
                    Order order = orderService.findByorderNo(out_trade_no); // 获取订单
                    if (order != null) {
                        // 设置支付状态已经支付
                        order.setStatus("已支付");
                        //设置支付宝订单号
                        order.setAliNo(trade_no);
                        orderService.save(order);
                        User byId = userService.findById(order.getUser().getId());
                        Calendar rightNow = Calendar.getInstance();
                        Date date = byId.getEndtime() == null ? new Date() : byId.getEndtime();
                        rightNow.setTime(date);
                        //设置VIP状态为true
                        byId.setVip(true);
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
                                byId.setVip(false);
                        }
                        //更新时间
                        byId.setEndtime(rightNow.getTime());
                        userService.save(byId);
                    }
                }
            }
            response.getWriter().write("success");
        } else {
            //验证失败
            response.getWriter().write("fail");
        }
        response.getWriter().flush();
        response.getWriter().close();
    }

}
