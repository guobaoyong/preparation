package qqzsh.top.preparation.project.content.virtualarticle.domain;

import lombok.Data;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-02-22 10:42
 * @description
 */
@Data
public class ImageLink {

    private String srcLink;

    public String toString() {
        return "ImageLink(srcLink=" + this.getSrcLink() + ")";
    }
}
