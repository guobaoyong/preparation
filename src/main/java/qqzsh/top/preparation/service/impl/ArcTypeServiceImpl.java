package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.ArcType;
import qqzsh.top.preparation.respository.ArcTypeRespository;
import qqzsh.top.preparation.service.ArcTypeService;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:10
 * @description 资源类别Service实现类
 */
@Service("arcTypeService")
public class ArcTypeServiceImpl implements ArcTypeService {

    @Autowired
    private ArcTypeRespository arcTypeRespository;

    @Override
    public List<ArcType> listAll(Sort.Direction direction, String... properties) {
        Sort sort=new Sort(direction, properties);
        return arcTypeRespository.findAll(sort);
    }

}
