package qqzsh.top.preparation.project.content.change.mapper;

import qqzsh.top.preparation.project.content.change.domain.PointChange;
import java.util.List;

/**
 * 积分变更记录Mapper接口
 * 
 * @author zsh
 * @date 2020-01-12
 */
public interface PointChangeMapper 
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
     * 删除积分变更记录
     * 
     * @param pointId 积分变更记录ID
     * @return 结果
     */
    public int deletePointChangeById(Long pointId);

    /**
     * 批量删除积分变更记录
     * 
     * @param pointIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePointChangeByIds(String[] pointIds);
}
