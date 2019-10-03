package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.Message;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 12:51
 * @Description 用户消息Respository接口
 */
public interface MessageRepository extends JpaRepository<Message, Integer>, JpaSpecificationExecutor<Message> {

    /**
     * 查询某个用户下的所有消息
     *
     * @param userId
     * @return
     */
    @Query(value = "select count(*) from t_message where is_see=false and user_id=?1", nativeQuery = true)
    Integer getCountByUserId(Integer userId);

    /**
     * 修改成已查看状态
     *
     * @param userId
     */
    @Query(value = "update t_message set is_see=true where user_id=?1", nativeQuery = true)
    @Modifying
    void updateState(Integer userId);

    /**
     * 通过userId删除消息
     */
    @Query(value = "delete from t_message where user_id=?1", nativeQuery = true)
    @Modifying
    void deleteByUserId(Integer userId);

}

