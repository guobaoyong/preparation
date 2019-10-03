package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.Link;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 17:19
 * @description 友情链接Service接口
 */
public interface LinkService {

    /**
     * 查询所有友情链接
     *
     * @param direction
     * @param properties
     * @return
     */
    List<Link> listAll(Sort.Direction direction, String... properties);

    /**
     * 根据条件分页查询友情链接信息
     *
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Link> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    /**
     * 根据条件查询总记录数
     *
     * @return
     */
    Long getTotal();

    /**
     * 根据id获取友情链接
     *
     * @param id
     * @return
     */
    Link get(Integer id);

    /**
     * 添加或者修改友情链接
     *
     * @param link
     */
    void save(Link link);

    /**
     * 根据id删除友情链接
     *
     * @param id
     */
    void delete(Integer id);
}
