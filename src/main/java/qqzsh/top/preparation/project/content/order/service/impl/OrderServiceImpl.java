package qqzsh.top.preparation.project.content.order.service.impl;

import java.util.List;
import qqzsh.top.preparation.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.project.content.order.mapper.OrderMapper;
import qqzsh.top.preparation.project.content.order.domain.Order;
import qqzsh.top.preparation.project.content.order.service.IOrderService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.order.service.IOrderService;
import qqzsh.top.preparation.project.system.user.service.IUserService;

/**
 * 订单Service业务层处理
 * 
 * @author zsh
 * @date 2020-01-02
 */
@Service
public class OrderServiceImpl implements IOrderService
{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询订单
     * 
     * @param id 订单ID
     * @return 订单
     */
    @Override
    public Order selectOrderById(Long id)
    {
        return orderMapper.selectOrderById(id);
    }

    /**
     * 查询订单列表
     * 
     * @param order 订单
     * @return 订单
     */
    @Override
    public List<Order> selectOrderList(Order order)
    {
        return orderMapper.selectOrderList(order);
    }

    /**
     * 新增订单
     * 
     * @param order 订单
     * @return 结果
     */
    @Override
    public int insertOrder(Order order) {
        order.setCreateTime(DateUtils.getNowDate());
        order.setDeptId(userService.selectUserById(order.getUserId()).getDeptId());
        int row = orderMapper.insertOrder(order);
        if (redisUtil.hasKey("orderNums")){
            Integer orderNums = (Integer) redisUtil.get("orderNums");
            redisUtil.delete("orderNums");
            redisUtil.set("orderNums",orderNums+1);
        }else {
            redisUtil.set("orderNums",selectOrderList(new Order()).size());
        }
        return row;
    }

    /**
     * 修改订单
     * 
     * @param order 订单
     * @return 结果
     */
    @Override
    public int updateOrder(Order order)
    {
        return orderMapper.updateOrder(order);
    }

    /**
     * 删除订单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOrderByIds(String ids)
    {
        int row = orderMapper.deleteOrderByIds(Convert.toStrArray(ids));
        if (redisUtil.hasKey("orderNums")){
            Integer orderNums = (Integer) redisUtil.get("orderNums");
            redisUtil.delete("orderNums");
            redisUtil.set("orderNums",orderNums-row);
        }else {
            redisUtil.set("orderNums",selectOrderList(new Order()).size());
        }
        return row;
    }

    /**
     * 删除订单信息
     * 
     * @param id 订单ID
     * @return 结果
     */
    @Override
    public int deleteOrderById(Long id) {
        int row = orderMapper.deleteOrderById(id);
        if (redisUtil.hasKey("orderNums")){
            Integer orderNums = (Integer) redisUtil.get("orderNums");
            redisUtil.delete("orderNums");
            redisUtil.set("orderNums",orderNums-1);
        }else {
            redisUtil.set("orderNums",selectOrderList(new Order()).size());
        }
        return row;
    }
}
