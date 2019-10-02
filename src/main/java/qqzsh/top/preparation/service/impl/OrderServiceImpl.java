package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.entity.Order;
import qqzsh.top.preparation.respository.CommentRepository;
import qqzsh.top.preparation.respository.OrderRepository;
import qqzsh.top.preparation.service.CommentService;
import qqzsh.top.preparation.service.OrderService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:57
 * @Description 订单Service实现类
 */
@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public List<Order> list(Order order, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page - 1, pageSize, direction, properties);
        Page<Order> pageOrder = orderRepository.findAll(new Specification<Order>() {

            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (order != null) {
                    if (order.getStatus() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), order.getStatus()));
                    }
                    if (order.getUserId() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("userId"), order.getUserId()));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageOrder.getContent();
    }

    @Override
    public Long getTotal(Order order) {
        Long count = orderRepository.count(new Specification<Order>() {

            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (order != null) {
                    if (order.getStatus() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), order.getStatus()));
                    }
                    if (order.getUserId() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("userId"), order.getUserId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public Order findByorderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }
}
