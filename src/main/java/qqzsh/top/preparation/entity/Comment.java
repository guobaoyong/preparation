package qqzsh.top.preparation.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:50
 * @Description 评论实体
 */
@Entity
@Table(name = "t_comment")
public class Comment implements Serializable {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    // 帖子
    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    // 评论人
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // 评论内容
    @Column(length = 1000)
    private String content;

    // 评论日期
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDate;

    // 审核状态 0  1 审核通过 2 审核未通过
    private Integer state;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


}

