package qqzsh.top.preparation.project.system.notice.mapper;

import qqzsh.top.preparation.project.system.notice.domain.Notice;
import java.util.List;

/**
 * 公告 数据层
 * 
 * @author zsh
 */
public interface NoticeMapper
{
    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    Notice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    List<Notice> selectNoticeList(Notice notice);

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    int insertNotice(Notice notice);

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    int updateNotice(Notice notice);

    /**
     * 批量删除公告
     * 
     * @param noticeIds 需要删除的数据ID
     * @return 结果
     */
    int deleteNoticeByIds(String[] noticeIds);

    /**
     * 获取最新一条通知
     * @return
     */
    Notice getNewOne();

    /**
     * 获取最新一条广告
     * @return
     */
    Notice getNewOneAD();

    /**
     * 获取上一条通知
     */
    Notice getFront(Long id);

    /**
     * 获取下一条通知
     */
    Notice getBack(Long id);
}