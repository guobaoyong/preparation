package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Notice;
import qqzsh.top.preparation.entity.Order;

import java.util.List;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-27 11:10
 * @description 通知接口
 */
public interface NoticeService {

    /**
     * 获取最新一条通知
     */
    Notice getNewOne();

    /**
     * 获取最新一条广告
     */
    Notice getNewOneAD();

    /**
     * 根据条件分页查询通知广告
     */
    List<Notice> list(Notice notice, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    /**
     * 根据条件获取总记录数
     */
    Long getTotal(Notice notice);

    /**
     * 删除通知广告
     */
    void delete(Integer id);

    /**
     * 根据id查找
     */
    Notice findById(Integer id);

    /**
     * 添加
     */
    void save(Notice notice);
}
