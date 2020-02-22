package qqzsh.top.preparation.project.content.virtualarticle.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.virtualarticle.mapper.VirtualarticleMapper;
import qqzsh.top.preparation.project.content.virtualarticle.domain.Virtualarticle;
import qqzsh.top.preparation.project.content.virtualarticle.service.IVirtualarticleService;
import qqzsh.top.preparation.common.utils.text.Convert;

/**
 * 爬虫记录Service业务层处理
 * 
 * @author zsh
 * @date 2020-02-22
 */
@Service
public class VirtualarticleServiceImpl implements IVirtualarticleService 
{
    @Autowired
    private VirtualarticleMapper virtualarticleMapper;

    /**
     * 查询爬虫记录
     * 
     * @param id 爬虫记录ID
     * @return 爬虫记录
     */
    @Override
    public Virtualarticle selectVirtualarticleById(Long id) {
        return virtualarticleMapper.selectVirtualarticleById(id);
    }

    /**
     * 查询爬虫记录列表
     * 
     * @param virtualarticle 爬虫记录
     * @return 爬虫记录
     */
    @Override
    public List<Virtualarticle> selectVirtualarticleList(Virtualarticle virtualarticle) {
        return virtualarticleMapper.selectVirtualarticleList(virtualarticle);
    }

    /**
     * 新增爬虫记录
     * 
     * @param virtualarticle 爬虫记录
     * @return 结果
     */
    @Override
    public int insertVirtualarticle(Virtualarticle virtualarticle) {
        return virtualarticleMapper.insertVirtualarticle(virtualarticle);
    }

    /**
     * 修改爬虫记录
     * 
     * @param virtualarticle 爬虫记录
     * @return 结果
     */
    @Override
    public int updateVirtualarticle(Virtualarticle virtualarticle) {
        return virtualarticleMapper.updateVirtualarticle(virtualarticle);
    }

    /**
     * 删除爬虫记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteVirtualarticleByIds(String ids) {
        return virtualarticleMapper.deleteVirtualarticleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除爬虫记录信息
     * 
     * @param id 爬虫记录ID
     * @return 结果
     */
    @Override
    public int deleteVirtualarticleById(Long id)
    {
        return virtualarticleMapper.deleteVirtualarticleById(id);
    }
}
