package qqzsh.top.preparation.project.monitor.logininfor.mapper;

import java.util.List;
import qqzsh.top.preparation.project.monitor.logininfor.domain.Logininfor;
import qqzsh.top.preparation.project.monitor.logininfor.domain.Logininfor;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author zsh
 */
public interface LogininforMapper
{
    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    public void insertLogininfor(Logininfor logininfor);

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public List<Logininfor> selectLogininforList(Logininfor logininfor);

    /**
     * 批量删除系统登录日志
     * 
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int deleteLogininforByIds(String[] ids);

    /**
     * 清空系统登录日志
     * 
     * @return 结果
     */
    public int cleanLogininfor();
}
