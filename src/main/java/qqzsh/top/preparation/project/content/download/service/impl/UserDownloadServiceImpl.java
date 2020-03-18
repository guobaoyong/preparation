package qqzsh.top.preparation.project.content.download.service.impl;

import java.util.List;

import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.project.content.comment.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.download.mapper.UserDownloadMapper;
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.project.system.user.service.IUserService;

/**
 * 用户已下载Service业务层处理
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Service
public class UserDownloadServiceImpl implements IUserDownloadService
{
    @Autowired
    private UserDownloadMapper userDownloadMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询用户已下载
     * 
     * @param downloadId 用户已下载ID
     * @return 用户已下载
     */
    @Override
    public UserDownload selectUserDownloadById(Long downloadId)
    {
        return userDownloadMapper.selectUserDownloadById(downloadId);
    }

    /**
     * 查询用户已下载列表
     * 
     * @param userDownload 用户已下载
     * @return 用户已下载
     */
    @Override
    public List<UserDownload> selectUserDownloadList(UserDownload userDownload) {
        return userDownloadMapper.selectUserDownloadList(userDownload);
    }

    /**
     * 新增用户已下载
     * 
     * @param userDownload 用户已下载
     * @return 结果
     */
    @Override
    public int insertUserDownload(UserDownload userDownload) {
        // 插入高校ID
        userDownload.setDeptId(userService.selectUserById(userDownload.getDownloadUserId()).getDeptId());
        int row = userDownloadMapper.insertUserDownload(userDownload);
        if (redisUtil.hasKey("downloadNums")){
            Integer downloadNums = (Integer) redisUtil.get("downloadNums");
            redisUtil.delete("downloadNums");
            redisUtil.set("downloadNums",downloadNums+1);
        }else {
            redisUtil.set("downloadNums",selectUserDownloadList(new UserDownload()).size());
        }
        return row;
    }

    /**
     * 修改用户已下载
     * 
     * @param userDownload 用户已下载
     * @return 结果
     */
    @Override
    public int updateUserDownload(UserDownload userDownload) {
        return userDownloadMapper.updateUserDownload(userDownload);
    }

    /**
     * 删除用户已下载对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserDownloadByIds(String ids)
    {
        int row = userDownloadMapper.deleteUserDownloadByIds(Convert.toStrArray(ids));
        if (redisUtil.hasKey("downloadNums")){
            Integer downloadNums = (Integer) redisUtil.get("downloadNums");
            redisUtil.delete("downloadNums");
            redisUtil.set("downloadNums",downloadNums-row);
        }else {
            redisUtil.set("downloadNums",selectUserDownloadList(new UserDownload()).size());
        }
        return row;
    }

    /**
     * 删除用户已下载信息
     * 
     * @param downloadId 用户已下载ID
     * @return 结果
     */
    @Override
    public int deleteUserDownloadById(Long downloadId)
    {
        int row = userDownloadMapper.deleteUserDownloadById(downloadId);
        if (redisUtil.hasKey("downloadNums")){
            Integer downloadNums = (Integer) redisUtil.get("downloadNums");
            redisUtil.delete("downloadNums");
            redisUtil.set("downloadNums",downloadNums-1);
        }else {
            redisUtil.set("downloadNums",selectUserDownloadList(new UserDownload()).size());
        }
        return row;
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        userDownloadMapper.deleteByArticleId(articleId);
        if (redisUtil.hasKey("downloadNums")){
            redisUtil.delete("downloadNums");
        }
        redisUtil.set("downloadNums",selectUserDownloadList(new UserDownload()).size());
    }

    @Override
    public List<UserDownload> selectJoint(String articleName, String loginName, String beginDownloadDate, String endDownloadDate, Long deptId) {
        return userDownloadMapper.selectJoint(articleName, loginName, beginDownloadDate, endDownloadDate, deptId);
    }
}
