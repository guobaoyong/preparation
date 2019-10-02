package qqzsh.top.preparation.service;

import org.springframework.data.domain.Sort;
import qqzsh.top.preparation.entity.UserDownload;

import java.util.List;

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

    /**
     * 根据条件分页查询用户下载信息
     * @param s_userDownload
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<UserDownload> list(UserDownload s_userDownload, Integer page, Integer pageSize, Sort.Direction direction, String...properties);


    /**
     * 根据条件查询总记录数
     * @param s_userDownload
     * @return
     */
    Long getTotal(UserDownload s_userDownload);

    /**
     * 根据id删除
     */
    void delete(Integer id);
}

