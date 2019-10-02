package qqzsh.top.preparation.controller.user;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.config.AliPayConfig;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.entity.Order;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.OrderService;
import qqzsh.top.preparation.service.UserService;
import qqzsh.top.preparation.util.PageUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 21:45
 * @Description 订单查询
 */
@Controller
@RequestMapping("/user/order")
public class OrderUserController {

    @Autowired
    private OrderService orderService;

    @Resource
    private AliPayConfig aliPayConfig;

    @Autowired
    private UserService userService;

    /**
     * 跳转到订单查询页面
     * @return
     */
    @RequestMapping("/toOrderPage")
    public ModelAndView toOrderPage(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "订单查询");
        mav.setViewName("user/listOrder");
        return mav;
    }

    /**
     * 根据条件分页查询订单信息
     * @param order
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/list")
    public Map<String,Object> list(HttpSession session,Order order, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="limit",required=false)Integer limit)throws Exception{
        User user=(User)session.getAttribute("currentUser");
        order.setUserId(user.getId());
        Map<String,Object> resultMap=new HashMap<String,Object>();
        List<Order> orderList = orderService.list(order, page, limit, Sort.Direction.DESC, "createTime");
        Long count = orderService.getTotal(order);
        resultMap.put("code", 0);
        resultMap.put("count", count);
        resultMap.put("data", orderList);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/check")
    public Map<String,Object> delete(Integer id,HttpSession session)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        //根据id查询信息
        Order byId = orderService.findById(id);
        if (byId.getStatus().equals("已支付")){
            resultMap.put("success", false);
        }else {
            //根据订单号查询支付状态
            String orderNo = byId.getOrderNo();
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.getUrl(), aliPayConfig.getAppid(), aliPayConfig.getRsa_private_key(), aliPayConfig.getFormat(), aliPayConfig.getCharset(), aliPayConfig.getAlipay_public_key(), aliPayConfig.getSigntype());
            //设置请求参数
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            AlipayTradePayModel model=new AlipayTradePayModel();
            // 设置订单号
            model.setOutTradeNo(orderNo);
            //请求
            alipayRequest.setBizModel(model);
            AlipayTradeQueryResponse result = alipayClient.execute(alipayRequest);
            String tradeStatus = result.getTradeStatus();
            if(tradeStatus.equals("TRADE_SUCCESS")){
                byId.setStatus("已支付");
                byId.setAliNo(result.getTradeNo());
                orderService.save(byId);
                User user = userService.findById(byId.getUserId());
                //根据金额去增加相应时间
                Calendar rightNow = Calendar.getInstance();
                Date date = user.getEndtime() == null?new Date():user.getEndtime();
                rightNow.setTime(date);
                //设置VIP状态为true
                user.setVip(true);
                //更新用户的VIP过期时间
                switch (result.getTotalAmount()){
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
                        user.setVip(false);
                }
                //更新时间
                user.setEndtime(rightNow.getTime());
                userService.save(user);
                //更新session
                session.setAttribute("currentUser",user);
                resultMap.put("success", true);
            }else {
                resultMap.put("success", false);
            }
        }
        return resultMap;
    }

}
