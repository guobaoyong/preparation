package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.Link;
import qqzsh.top.preparation.respository.LinkRepository;
import qqzsh.top.preparation.service.LinkService;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 17:19
 * @description 友情链接Service实现类
 */
@Service("linkService")
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Link> listAll(Direction direction, String... properties) {
        Sort sort=new Sort(direction,properties);
        return linkRepository.findAll(sort);
    }

}
