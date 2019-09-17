package qqzsh.top.preparation.entity;

import javax.persistence.*;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 17:15
 * @description 友情链接实体
 */
@Entity
@Table(name="t_link")
public class Link {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    // 名称
    @Column(length=500)
    private String name;

    // 链接地址
    @Column(length=500)
    private String url;

    // 排序
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

