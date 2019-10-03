package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Sort sort = new Sort(direction, properties);
        return linkRepository.findAll(sort);
    }

    @Override
    public List<Link> list(Integer page, Integer pageSize, Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page - 1, pageSize, direction, properties);
        Page<Link> pageLink = linkRepository.findAll(pageable);
        return pageLink.getContent();
    }

    @Override
    public Long getTotal() {
        return linkRepository.count();
    }

    @Override
    public Link get(Integer id) {
        return linkRepository.findOne(id);
    }

    @Override
    public void save(Link link) {
        linkRepository.save(link);
    }

    @Override
    public void delete(Integer id) {
        linkRepository.delete(id);
    }

}
