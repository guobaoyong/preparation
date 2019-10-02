package qqzsh.top.preparation.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import qqzsh.top.preparation.entity.ArcType;
import qqzsh.top.preparation.entity.Link;
import qqzsh.top.preparation.service.ArcTypeService;
import qqzsh.top.preparation.service.LinkService;
import qqzsh.top.preparation.service.UserService;
import qqzsh.top.preparation.util.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:13
 * @description 初始化加载数据
 */
@Component("initSystem")
public class InitSystem implements ServletContextListener, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Map<Integer, ArcType> arcTypeMap = new HashMap<>();

    @Resource
    private RedisUtil<Integer> redisUtil;

    /**
     * 加载数据到application缓存中
     *
     * @param application
     */
    public void loadData(ServletContext application) {
        ArcTypeService arcTypeService = (ArcTypeService) applicationContext.getBean("arcTypeService");
        LinkService linkService = (LinkService) applicationContext.getBean("linkService");
        List<ArcType> allArcTypeList = arcTypeService.listAll(Sort.Direction.ASC, "sort");
        List<Link> allLinkList = linkService.listAll(Sort.Direction.ASC, "sort");
        for (ArcType arcType : allArcTypeList) {
            arcTypeMap.put(arcType.getId(), arcType);
        }
        application.setAttribute("allArcTypeList", allArcTypeList);
        application.setAttribute("allLinkList", allLinkList);

        if (redisUtil.get("signTotal") != null) {
            Integer signTotal = (Integer) redisUtil.get("signTotal");
            application.setAttribute("signTotal", signTotal);
        } else {
            redisUtil.set("signTotal", 0);
            application.setAttribute("signTotal", 0);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.loadData(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
