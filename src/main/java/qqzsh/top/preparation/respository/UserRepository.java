package qqzsh.top.preparation.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import qqzsh.top.preparation.entity.User;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-20 11:33
 * @description 用户Respository接口
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查找用户实体
     *
     * @param userName
     * @return
     */
    @Query(value = "select * from t_user where user_name=?1", nativeQuery = true)
    User findByUserName(String userName);

    /**
     * 根据邮箱查找用户实体
     *
     * @param email
     * @return
     */
    @Query(value = "select * from t_user where email=?1", nativeQuery = true)
    User findByEmail(String email);

    /**
     * 重置所有签到信息
     */
    @Query(value = "update t_user set is_sign=false,sign_sort=null,sign_time=null", nativeQuery = true)
    @Modifying
    void updateAllSignInfo();
}
