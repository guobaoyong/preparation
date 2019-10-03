package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Order;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 17:18
 * @description 订单Respository接口
 */
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    /**
     * 根据订单号查找订单实体
     *
     * @param orderNo
     * @return
     */
    @Query(value = "select * from t_order where order_no=?1", nativeQuery = true)
    Order findByOrderNo(String orderNo);
}
