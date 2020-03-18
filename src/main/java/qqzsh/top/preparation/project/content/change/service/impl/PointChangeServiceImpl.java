package qqzsh.top.preparation.project.content.change.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.change.mapper.PointChangeMapper;
import qqzsh.top.preparation.project.content.change.domain.PointChange;
import qqzsh.top.preparation.project.content.change.service.IPointChangeService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.system.user.service.IUserService;

/**
 * 积分变更记录Service业务层处理
 * 
 * @author zsh
 * @date 2020-01-12
 */
@Service
public class PointChangeServiceImpl implements IPointChangeService 
{
    @Autowired
    private PointChangeMapper pointChangeMapper;

    @Autowired
    private IUserService userService;

    /**
     * 查询积分变更记录
     * 
     * @param pointId 积分变更记录ID
     * @return 积分变更记录
     */
    @Override
    public PointChange selectPointChangeById(Long pointId)
    {
        return pointChangeMapper.selectPointChangeById(pointId);
    }

    /**
     * 查询积分变更记录列表
     * 
     * @param pointChange 积分变更记录
     * @return 积分变更记录
     */
    @Override
    public List<PointChange> selectPointChangeList(PointChange pointChange)
    {
        return pointChangeMapper.selectPointChangeList(pointChange);
    }

    /**
     * 新增积分变更记录
     * 
     * @param pointChange 积分变更记录
     * @return 结果
     */
    @Override
    public int insertPointChange(PointChange pointChange)
    {
        pointChange.setDeptId(userService.selectUserById(pointChange.getPointUserId()).getDeptId());
        return pointChangeMapper.insertPointChange(pointChange);
    }

    /**
     * 修改积分变更记录
     * 
     * @param pointChange 积分变更记录
     * @return 结果
     */
    @Override
    public int updatePointChange(PointChange pointChange)
    {
        return pointChangeMapper.updatePointChange(pointChange);
    }

    /**
     * 删除积分变更记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePointChangeByIds(String ids)
    {
        return pointChangeMapper.deletePointChangeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除积分变更记录信息
     * 
     * @param pointId 积分变更记录ID
     * @return 结果
     */
    @Override
    public int deletePointChangeById(Long pointId)
    {
        return pointChangeMapper.deletePointChangeById(pointId);
    }
}
