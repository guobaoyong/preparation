package qqzsh.top.preparation.project.monitor.job.task;

import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.project.system.user.service.IUserService;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-01-04 21:01
 * @description 清除今日签到信息
 */
@Component("RemoveSign")
public class RemoveSign {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    public void ryNoParams() {
        userService.updateAllSignInfo();
        if (redisUtil.hasKey("signTotal")){
            redisUtil.delete("signTotal");
        }
        redisUtil.set("signTotal",0);
    }

}
