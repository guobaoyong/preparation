package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Comment;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:54
 * @Description 评论Respository接口
 */
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    /**
     * 删除指定帖子的评论信息
     * @param articleId
     */
    @Query(value="delete from t_comment where article_id=?1",nativeQuery=true)
    @Modifying
    void deleteByArticleId(Integer articleId);
}
