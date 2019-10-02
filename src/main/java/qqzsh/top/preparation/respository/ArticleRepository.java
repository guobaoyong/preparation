package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.User;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:39
 * @description 资源Respository接口
 */
public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

}
