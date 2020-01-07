package qqzsh.top.preparation.project.content.type.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Excel;
import qqzsh.top.preparation.framework.web.domain.BaseEntity;

/**
 * 资源类别对象 sys_arc_type
 * 
 * @author zsh
 * @date 2019-12-30
 */
public class ArcType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源类别主键ID */
    private Long srcTypeId;

    /** 资源类别名称 */
    @Excel(name = "资源类别名称")
    private String srcTypeName;

    /** 资源类别备注 */
    @Excel(name = "资源类别备注")
    private String srcTypeRemark;

    /** 资源类别排序 */
    @Excel(name = "资源类别排序")
    private Long srcTypeSort;

    public void setSrcTypeId(Long srcTypeId) 
    {
        this.srcTypeId = srcTypeId;
    }

    public Long getSrcTypeId() 
    {
        return srcTypeId;
    }
    public void setSrcTypeName(String srcTypeName) 
    {
        this.srcTypeName = srcTypeName;
    }

    public String getSrcTypeName() 
    {
        return srcTypeName;
    }
    public void setSrcTypeRemark(String srcTypeRemark) 
    {
        this.srcTypeRemark = srcTypeRemark;
    }

    public String getSrcTypeRemark() 
    {
        return srcTypeRemark;
    }
    public void setSrcTypeSort(Long srcTypeSort) 
    {
        this.srcTypeSort = srcTypeSort;
    }

    public Long getSrcTypeSort() 
    {
        return srcTypeSort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("srcTypeId", getSrcTypeId())
            .append("srcTypeName", getSrcTypeName())
            .append("srcTypeRemark", getSrcTypeRemark())
            .append("srcTypeSort", getSrcTypeSort())
            .toString();
    }
}
