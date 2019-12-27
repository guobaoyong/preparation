package qqzsh.top.preparation.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-27 11:04
 * @description 通知实体
 */
@Entity
@Table(name = "t_notice")
@Data
public class Notice implements Serializable {

    // 编号
    @Id
    @GeneratedValue
    private Integer id;

    // 通知名称
    @Column(length = 200)
    private String name;

    // 通知内容
    @Lob
    @Column(columnDefinition = "longtext")
    private String content;

    // 发布日期
    private Date publishDate;

    //类型
    private String type;
}
