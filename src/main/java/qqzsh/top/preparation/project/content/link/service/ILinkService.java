package qqzsh.top.preparation.project.content.link.service;

import qqzsh.top.preparation.project.content.link.domain.Link;
import java.util.List;

/**
 * 友链Service接口
 * 
 * @author zsh
 * @date 2019-12-31
 */
public interface ILinkService 
{
    /**
     * 查询友链
     * 
     * @param linkId 友链ID
     * @return 友链
     */
    public Link selectLinkById(Long linkId);

    /**
     * 查询友链列表
     * 
     * @param link 友链
     * @return 友链集合
     */
    public List<Link> selectLinkList(Link link);

    /**
     * 新增友链
     * 
     * @param link 友链
     * @return 结果
     */
    public int insertLink(Link link);

    /**
     * 修改友链
     * 
     * @param link 友链
     * @return 结果
     */
    public int updateLink(Link link);

    /**
     * 批量删除友链
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLinkByIds(String ids);

    /**
     * 删除友链信息
     * 
     * @param linkId 友链ID
     * @return 结果
     */
    public int deleteLinkById(Long linkId);
}
