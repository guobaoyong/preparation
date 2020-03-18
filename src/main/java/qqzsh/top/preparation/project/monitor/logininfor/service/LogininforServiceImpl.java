package qqzsh.top.preparation.project.monitor.logininfor.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.monitor.logininfor.domain.Logininfor;
import qqzsh.top.preparation.project.monitor.logininfor.mapper.LogininforMapper;
import qqzsh.top.preparation.project.system.dept.domain.Dept;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author zsh
 */
@Service
public class LogininforServiceImpl implements ILogininforService
{
    @Autowired
    private LogininforMapper logininforMapper;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;

    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(Logininfor logininfor) {
        // 根据username 查询用户信息
        try {
            User user = userService.selectUserByLoginName(logininfor.getLoginName());
            if (user != null){
                // 查询高校信息
                Dept dept = deptService.selectDeptById(user.getDeptId());
                if (dept != null){
                    logininfor.setDeptName(dept.getDeptName());
                }
            }else {
                logininfor.setDeptName("未知高校");
            }
        }catch (Exception e){
            logininfor.setDeptName("未知高校");
        }
        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<Logininfor> selectLogininforList(Logininfor logininfor)
    {
        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int deleteLogininforByIds(String ids)
    {
        return logininforMapper.deleteLogininforByIds(Convert.toStrArray(ids));
    }
    
    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
