package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Notice;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-27 11:09
 * @description 通知Respository接口
 */
public interface NoticeRepository extends JpaRepository<Notice, Integer>, JpaSpecificationExecutor<Notice> {

    /**
     * 获取最新一条通知
     * @return
     */
    @Query(value = "select * from t_notice where type = 'notice' order by id desc limit 1", nativeQuery = true)
    Notice getNewOne();

    /**
     * 获取最新一条广告
     * @return
     */
    @Query(value = "select * from t_notice where type = 'ad' order by id desc limit 1", nativeQuery = true)
    Notice getNewOneAD();

}
