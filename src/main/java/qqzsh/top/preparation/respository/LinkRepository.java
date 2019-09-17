package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import qqzsh.top.preparation.entity.Link;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 17:18
 * @description 友情链接Respository接口
 */
public interface LinkRepository extends JpaRepository<Link, Integer>, JpaSpecificationExecutor<Link> {

}
