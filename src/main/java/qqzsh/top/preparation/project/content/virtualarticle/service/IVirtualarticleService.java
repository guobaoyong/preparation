package qqzsh.top.preparation.project.content.virtualarticle.service;

import qqzsh.top.preparation.project.content.virtualarticle.domain.Virtualarticle;
import java.util.List;

/**
 * 爬虫记录Service接口
 * 
 * @author zsh
 * @date 2020-02-22
 */
public interface IVirtualarticleService 
{
    /**
     * 查询爬虫记录
     * 
     * @param id 爬虫记录ID
     * @return 爬虫记录
     */
    Virtualarticle selectVirtualarticleById(Long id);

    /**
     * 查询爬虫记录列表
     * 
     * @param virtualarticle 爬虫记录
     * @return 爬虫记录集合
     */
    List<Virtualarticle> selectVirtualarticleList(Virtualarticle virtualarticle);

    /**
     * 新增爬虫记录
     * 
     * @param virtualarticle 爬虫记录
     * @return 结果
     */
    int insertVirtualarticle(Virtualarticle virtualarticle);

    /**
     * 修改爬虫记录
     * 
     * @param virtualarticle 爬虫记录
     * @return 结果
     */
    int updateVirtualarticle(Virtualarticle virtualarticle);

    /**
     * 批量删除爬虫记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteVirtualarticleByIds(String ids);

    /**
     * 删除爬虫记录信息
     * 
     * @param id 爬虫记录ID
     * @return 结果
     */
    int deleteVirtualarticleById(Long id);
}
