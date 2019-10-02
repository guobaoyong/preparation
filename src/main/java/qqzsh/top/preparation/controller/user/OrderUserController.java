package qqzsh.top.preparation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.entity.Order;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.OrderService;
import qqzsh.top.preparation.util.PageUtil;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
