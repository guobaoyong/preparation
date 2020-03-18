package qqzsh.top.preparation.project.content.download.mapper;

import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import java.util.List;

/**
 * 用户已下载Mapper接口
 * 
 * @author zsh
 * @date 2019-12-31
 */
public interface UserDownloadMapper 
{
    /**
     * 查询用户已下载
     * 
     * @param downloadId 用户已下载ID
     * @return 用户已下载
     */
    public UserDownload selectUserDownloadById(Long downloadId);

    /**
     * 查询用户已下载列表
     * 
     * @param userDownload 用户已下载
     * @return 用户已下载集合
     */
    public List<UserDownload> selectUserDownloadList(UserDownload userDownload);

    /**
     * 新增用户已下载
     * 
     * @param userDownload 用户已下载
     * @return 结果
     */
    public int insertUserDownload(UserDownload userDownload);

    /**
     * 修改用户已下载
     * 
     * @param userDownload 用户已下载
     * @return 结果
     */
    public int updateUserDownload(UserDownload userDownload);

    /**
     * 删除用户已下载
     * 
     * @param downloadId 用户已下载ID
     * @return 结果
     */
    public int deleteUserDownloadById(Long downloadId);

    /**
     * 批量删除用户已下载
     * 
     * @param downloadIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserDownloadByIds(String[] downloadIds);

    /**
     * 删除指定帖子的下载信息
     *
     * @param articleId
     */
    void deleteByArticleId(Long articleId);

    /**
     * 联表查询
     * @param articleName
     * @param loginName
     * @return
     */
    List<UserDownload> selectJoint(String articleName, String loginName, String beginDownloadDate, String endDownloadDate, Long deptId);
}
