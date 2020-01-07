package qqzsh.top.preparation.project.content.link.service.impl;

import java.util.List;

import qqzsh.top.preparation.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.link.mapper.LinkMapper;
import qqzsh.top.preparation.project.content.link.domain.Link;
import qqzsh.top.preparation.project.content.link.service.ILinkService;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.link.domain.Link;
import qqzsh.top.preparation.project.content.link.mapper.LinkMapper;
import qqzsh.top.preparation.project.content.link.service.ILinkService;

/**
 * 友链Service业务层处理
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Service
public class LinkServiceImpl implements ILinkService
{
    @Autowired
    private LinkMapper linkMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询友链
     * 
     * @param linkId 友链ID
     * @return 友链
     */
    @Override
    public Link selectLinkById(Long linkId)
    {
        return linkMapper.selectLinkById(linkId);
    }

    /**
     * 查询友链列表
     * 
     * @param link 友链
     * @return 友链
     */
    @Override
    public List<Link> selectLinkList(Link link)
    {
        return linkMapper.selectLinkList(link);
    }

    /**
     * 新增友链
     * 
     * @param link 友链
     * @return 结果
     */
    @Override
    public int insertLink(Link link){
        int row = linkMapper.insertLink(link);
        this.updateRedis();
        return row;
    }

    /**
     * 修改友链
     * 
     * @param link 友链
     * @return 结果
     */
    @Override
    public int updateLink(Link link) {
        int row = linkMapper.updateLink(link);
        this.updateRedis();
        return row;
    }

    /**
     * 删除友链对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLinkByIds(String ids) {
        int row = linkMapper.deleteLinkByIds(Convert.toStrArray(ids));
        this.updateRedis();
        return row;
    }

    /**
     * 删除友链信息
     * 
     * @param linkId 友链ID
     * @return 结果
     */
    @Override
    public int deleteLinkById(Long linkId) {
        int row = linkMapper.deleteLinkById(linkId);
        this.updateRedis();
        return row;
    }

    /**
     * 更新redis中的友情链接
     */
    public void updateRedis(){
        if (redisUtil.hasKey("link_list")){
            redisUtil.delete("link_list");
        }
        // 从数据库中查询所有友情链接并放入redis
        List<Link> links = selectLinkList(new Link());
        redisUtil.set("link_list",links);
    }
}
