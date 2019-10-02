package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.entity.Order;

import java.util.List;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:56
 * @Description 订单Service接口
 */
public interface OrderService {

    /**
     * 添加或者修改订单
     * @param order
     */
    void save(Order order);

    /**
     * 根据条件分页查询评论订单
     * @param order
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Order> list(Order order, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    /**
     * 根据条件获取总记录数
     * @param order
     * @return
     */
    Long getTotal(Order order);

    /**
     * 通过订单号查询信息
     * @param orderNo
     * @return
     */
    Order findByorderNo(String orderNo);
}

