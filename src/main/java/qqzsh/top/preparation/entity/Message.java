package qqzsh.top.preparation.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 12:48
 * @Description 用户消息实体
 */
@Entity
@Table(name = "t_message")
public class Message implements Serializable {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    // 消息内容
    @Column(length = 100)
    private String content;

    // 发布日期
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    // 所属用户
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // 消息是否被查看 true是 false 否
    private boolean isSee = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSee() {
        return isSee;
    }

    public void setSee(boolean isSee) {
        this.isSee = isSee;
    }
}

