package qqzsh.top.preparation.project.content.change.service;

import qqzsh.top.preparation.project.content.change.domain.PointChange;
import java.util.List;

/**
 * 积分变更记录Service接口
 * 
 * @author zsh
 * @date 2020-01-12
 */
public interface IPointChangeService 
{
    /**
     * 查询积分变更记录
     * 
     * @param pointId 积分变更记录ID
     * @return 积分变更记录
     */
    public PointChange selectPointChangeById(Long pointId);

    /**
     * 查询积分变更记录列表
     * 
     * @param pointChange 积分变更记录
     * @return 积分变更记录集合
     */
    public List<PointChange> selectPointChangeList(PointChange pointChange);

    /**
     * 新增积分变更记录
     * 
     * @param pointChange 积分变更记录
     * @return 结果
     */
    public int insertPointChange(PointChange pointChange);

    /**
     * 修改积分变更记录
     * 
     * @param pointChange 积分变更记录
     * @return 结果
     */
    public int updatePointChange(PointChange pointChange);

    /**
     * 批量删除积分变更记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePointChangeByIds(String ids);

    /**
     * 删除积分变更记录信息
     * 
     * @param pointId 积分变更记录ID
     * @return 结果
     */
    public int deletePointChangeById(Long pointId);
}
