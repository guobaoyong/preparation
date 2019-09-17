package qqzsh.top.preparation.entity;

import javax.persistence.*;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:04
 * @description 资源类型
 */
@Entity
@Table(name="t_arcType")
public class ArcType {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    // 资源类型名称
    @Column(length=100)
    private String name;

    // 描述
    @Column(length=1000)
    private String remark;

    // 排序（从小到大排序）
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
