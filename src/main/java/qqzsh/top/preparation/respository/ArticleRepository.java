package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Article;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:39
 * @description 资源Respository接口
 */
public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    List<Article> findByNameLike(String name);

    @Query(value = "select * from t_article where user_id=?1", nativeQuery = true)
    List<Article> findByUserId(Integer userId);
}
