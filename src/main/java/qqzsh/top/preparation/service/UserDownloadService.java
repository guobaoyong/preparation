package qqzsh.top.preparation.service;

import qqzsh.top.preparation.entity.UserDownload;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:06
 * @Description 用户下载Service接口
 */
public interface UserDownloadService {

    /**
     * 查询某个用户下载某个资源的次数
     * @param userId
     * @param articleId
     * @return
     */
    Integer getCountByUserIdAndArticleId(Integer userId,Integer articleId);

    /**
     * 添加或者修改用户下载信息
     * @param userDownload
     */
    void save(UserDownload userDownload);

    /**
     * 删除指定帖子的下载信息
     * @param articleId
     */
    void deleteByArticleId(Integer articleId);

}

