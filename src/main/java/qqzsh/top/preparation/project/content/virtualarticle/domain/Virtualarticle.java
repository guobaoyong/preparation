package qqzsh.top.preparation.project.content.virtualarticle.domain;

import lombok.Data;
import me.zhyd.hunter.entity.ImageLink;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 爬虫记录对象 sys_virtualarticle
 * 
 * @author zsh
 * @date 2020-02-22
 */
@Data
public class Virtualarticle extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 题名 */
    @Excel(name = "题名")
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 作者 */
    @Excel(name = "作者")
    private String author;

    /** 发布时间 */
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date releasedate;

    /** 来源 */
    @Excel(name = "来源")
    private String source;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 关键词 */
    @Excel(name = "关键词")
    private String keywords;

    /** 标签 */
    @Excel(name = "标签")
    private String tags;

    /** 图片链接 */
    @Excel(name = "图片链接")
    private String imagelinks;
}
