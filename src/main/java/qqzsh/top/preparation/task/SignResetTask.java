package qqzsh.top.preparation.task;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import qqzsh.top.preparation.service.UserService;
import qqzsh.top.preparation.util.RedisUtil;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 14:52
 * @Description 每天凌晨0点 重置用户所有签到信息
 */
@Component
public class SignResetTask implements ServletContextListener {

    @Autowired
    private UserService userSerivce;

    @Resource
    private RedisUtil<Integer> redisUtil;

    private static ServletContext application;


    @Scheduled(cron = "0 0 0 * * ?")
    private void process() {
        application.setAttribute("signTotal", 0);
        redisUtil.set("signTotal", 0);
        userSerivce.updateAllSignInfo();
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        application = sce.getServletContext();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}

