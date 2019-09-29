package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.UserDownload;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:03
 * @Description 用户下载资源Respository接口
 */
public interface UserDownloadRepository extends JpaRepository<UserDownload, Integer>, JpaSpecificationExecutor<UserDownload> {

    /**
     * 查询某个用户下载某个资源的次数
     * @param userId
     * @param articleId
     * @return
     */
    @Query(value="select count(*) from t_user_download where user_id=?1 and article_id=?2",nativeQuery=true)
    Integer getCountByUserIdAndArticleId(Integer userId,Integer articleId);

    /**
     * 删除指定帖子的下载信息
     * @param articleId
     */
    @Query(value="delete from t_user_download where article_id=?1",nativeQuery=true)
    @Modifying
    public void deleteByArticleId(Integer articleId);

}

