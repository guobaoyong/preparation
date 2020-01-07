package qqzsh.top.preparation.project.content.type.service;

import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.domain.ArcType;

import java.util.List;

/**
 * 资源类别Service接口
 * 
 * @author zsh
 * @date 2019-12-30
 */
public interface IArcTypeService 
{
    /**
     * 查询资源类别
     * 
     * @param srcTypeId 资源类别ID
     * @return 资源类别
     */
    public ArcType selectArcTypeById(Long srcTypeId);

    /**
     * 查询资源类别列表
     * 
     * @param arcType 资源类别
     * @return 资源类别集合
     */
    public List<ArcType> selectArcTypeList(ArcType arcType);

    /**
     * 新增资源类别
     * 
     * @param arcType 资源类别
     * @return 结果
     */
    public int insertArcType(ArcType arcType);

    /**
     * 修改资源类别
     * 
     * @param arcType 资源类别
     * @return 结果
     */
    public int updateArcType(ArcType arcType);

    /**
     * 批量删除资源类别
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArcTypeByIds(String ids);

    /**
     * 删除资源类别信息
     * 
     * @param srcTypeId 资源类别ID
     * @return 结果
     */
    public int deleteArcTypeById(Long srcTypeId);
}
