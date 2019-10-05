package qqzsh.top.preparation.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 20:58
 * @Description 用户下载实体
 */
@Entity
@Table(name = "t_userDownload")
public class UserDownload implements Serializable {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    // 下载资源
    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    // 下载用户
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // 下载日期
    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }


}

