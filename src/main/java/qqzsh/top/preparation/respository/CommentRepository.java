package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import qqzsh.top.preparation.entity.Comment;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:54
 * @Description 评论Respository接口
 */
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

}
