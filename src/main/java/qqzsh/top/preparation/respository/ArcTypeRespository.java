package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import qqzsh.top.preparation.entity.ArcType;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:08
 * @description 资源类型Respository接口
 */
public interface ArcTypeRespository extends JpaRepository<ArcType, Integer>, JpaSpecificationExecutor<ArcType> {

}
