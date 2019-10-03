package qqzsh.top.preparation.controller.admin;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.config.AliPayConfig;
import qqzsh.top.preparation.entity.Message;
import qqzsh.top.preparation.entity.Order;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.OrderService;
import qqzsh.top.preparation.service.UserService;
import qqzsh.top.preparation.util.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-03 13:15
 * @Description 订单控制器
 */
@Controller
@RequestMapping("/admin/order")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private RedisUtil<Integer> redisNum;

    @ResponseBody
    @RequiresPermissions(value={"分页查询订单信息"})
    @RequestMapping("/list")
    public Map<String,Object> list(Order order,
                                   @RequestParam(value="page",required=false)Integer page,
                                   @RequestParam(value="limit",required=false)Integer limit,
                                   @RequestParam(value="userName",required=false)String userName)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        if (userName != null && userName != ""){
            User user = userService.findByUserName(userName);
            if (user != null){
                order.setUser(user);
            }else {
                resultMap.put("code", 0);
                resultMap.put("count", 0L);
                resultMap.put("data", new ArrayList<>());
                return resultMap;
            }
        }
        List<Order> orderList = orderService.list(order,page, limit, Sort.Direction.DESC, "createTime");
        List<Order> newList = new ArrayList<>();
        orderList.forEach( order1 -> {
            order1.setUser(userService.getById(order1.getUser().getId()));
            newList.add(order1);
        });
        Long total = orderService.getTotal(order);
        resultMap.put("code", 0);
        resultMap.put("count", total);
        resultMap.put("data", newList);
        return resultMap;
    }

    /**
     * 根据id删除消息
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(value={"删除订单信息"})
    public Map<String,Object> delete(Integer id)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        //删除订单信息
        orderService.delete(id);
        //更新索redis
        if (!redisNum.hasKey("orderNums")) {
            redisNum.set("orderNums", orderService.getTotal(null).intValue());
        } else {
            int num = (int) redisNum.get("orderNums");
            redisNum.del("orderNums");
            redisNum.set("orderNums", num - 1);
        }
        resultMap.put("success", true);
        return resultMap;
    }


    /**
     * 多选删除
     * @param ids
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/deleteSelected")
    @RequiresPermissions(value={"删除消息信息"})
    public Map<String,Object> deleteSelected(String ids)throws Exception{
        String[] idsStr = ids.split(",");
        for(int i=0;i<idsStr.length;i++){
            // 删除订单信息
            orderService.delete(Integer.parseInt(idsStr[i]));
        }
        //更新索redis
        if (!redisNum.hasKey("orderNums")) {
            redisNum.set("orderNums", orderService.getTotal(null).intValue());
        } else {
            int num = (int) redisNum.get("orderNums");
            redisNum.del("orderNums");
            redisNum.set("orderNums", num - idsStr.length);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/check")
    @RequiresPermissions(value={"复议订单"})
    public Map<String,Object> updateStatus(Integer id, HttpSession session)throws Exception{
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
            if("TRADE_SUCCESS".equals(tradeStatus)){
                byId.setStatus("已支付");
                byId.setAliNo(result.getTradeNo());
                orderService.save(byId);
                User user = userService.findById(byId.getUser().getId());
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
                if (user.getId() == byId.getUser().getId()){
                    session.setAttribute("currentUser",user);
                }

                resultMap.put("success", true);
            }else {
                resultMap.put("success", false);
            }
        }
        return resultMap;
    }



}
