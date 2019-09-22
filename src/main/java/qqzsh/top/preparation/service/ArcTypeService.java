package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.ArcType;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:09
 * @description 资源类别Service接口
 */
public interface ArcTypeService {

    /**
     * 查询所有资源类别
     * @param direction
     * @param properties
     * @return
     */
    List<ArcType> listAll(Sort.Direction direction, String...properties);

    /**
     * 根据条件分页查询资源类别信息
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<ArcType> list(Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    /**
     * 根据条件查询总记录数
     * @return
     */
    Long getTotal();

    /**
     * 根据id获取资源类别
     * @param id
     * @return
     */
    ArcType get(Integer id);

    /**
     * 添加或者修改资源类别
     * @param arcType
     */
    void save(ArcType arcType);

    /**
     * 根据id删除资源类别
     * @param id
     */
    void delete(Integer id);

}
