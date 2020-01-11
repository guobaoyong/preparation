package qqzsh.top.preparation.project.content.type.domain;

import lombok.Data;
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
@Data
public class ArcType extends BaseEntity {

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

    /** 每个类别所含资源数量 */
    private Integer allSize;

    /** 热门每个类别所含资源数量 */
    private Integer hotSize;

}
