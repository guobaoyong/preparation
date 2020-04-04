package qqzsh.top.preparation.project.system.notice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.system.notice.mapper.NoticeMapper;
import qqzsh.top.preparation.project.system.notice.domain.Notice;
import qqzsh.top.preparation.project.system.notice.service.INoticeService;
import qqzsh.top.preparation.project.system.notice.mapper.NoticeMapper;

/**
 * 公告 服务层实现
 * 
 * @author zsh
 * @date 2018-06-25
 */
@Service
public class NoticeServiceImpl implements INoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public Notice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<Notice> selectNoticeList(Notice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(Notice notice)
    {
        notice.setCreateBy(ShiroUtils.getLoginName());
        int row = noticeMapper.insertNotice(notice);
        updateRedis();
        return row;
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(Notice notice)
    {
        notice.setUpdateBy(ShiroUtils.getLoginName());
        int row = noticeMapper.updateNotice(notice);
        updateRedis();
        return row;
    }

    /**
     * 删除公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String ids) {
        int row = noticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
        updateRedis();
        return row;
    }

    @Override
    public Notice getNewOne() {
        return noticeMapper.getNewOne();
    }

    @Override
    public Notice getNewOneAD() {
        return noticeMapper.getNewOneAD();
    }

    @Override
    public Notice getFront(Long id) {
        return noticeMapper.getFront(id);
    }

    @Override
    public Notice getBack(Long id) {
        return noticeMapper.getBack(id);
    }

    /**
     * 更新redis数据
     */
    private void updateRedis(){

        // 获取最新一条通知
        if (redisUtil.hasKey("notice")){
            redisUtil.delete("notice");
        }
        redisUtil.set("notice",getNewOne());

        // 获取最新一条广告
        if (redisUtil.hasKey("ad")){
            redisUtil.delete("ad");
        }
        redisUtil.set("ad",getNewOneAD());
    }
}
