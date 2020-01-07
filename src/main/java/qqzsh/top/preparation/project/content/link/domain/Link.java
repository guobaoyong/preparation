package qqzsh.top.preparation.project.content.link.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;

/**
 * 友链对象 sys_link
 * 
 * @author zsh
 * @date 2019-12-31
 */
public class Link extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 友链ID */
    private Long linkId;

    /** 名称 */
    @Excel(name = "名称")
    private String linkName;

    /** 排序 */
    @Excel(name = "排序")
    private Long linkSort;

    /** 地址 */
    @Excel(name = "地址")
    private String linkUrl;

    public void setLinkId(Long linkId) 
    {
        this.linkId = linkId;
    }

    public Long getLinkId() 
    {
        return linkId;
    }
    public void setLinkName(String linkName) 
    {
        this.linkName = linkName;
    }

    public String getLinkName() 
    {
        return linkName;
    }
    public void setLinkSort(Long linkSort) 
    {
        this.linkSort = linkSort;
    }

    public Long getLinkSort() 
    {
        return linkSort;
    }
    public void setLinkUrl(String linkUrl) 
    {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrl() 
    {
        return linkUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("linkId", getLinkId())
            .append("linkName", getLinkName())
            .append("linkSort", getLinkSort())
            .append("linkUrl", getLinkUrl())
            .toString();
    }
}
