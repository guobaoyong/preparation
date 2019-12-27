package qqzsh.top.preparation.service;

import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Notice;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-27 11:10
 * @description 通知接口
 */
public interface NoticeService {

    /**
     * 获取最新一条通知
     */
    Notice getNewOne();

    /**
     * 获取最新一条广告
     */
    Notice getNewOneAD();

}
