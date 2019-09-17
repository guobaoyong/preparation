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
     * @param direction
     * @param properties
     * @return
     */
    List<Link> listAll(Sort.Direction direction, String...properties);

}
