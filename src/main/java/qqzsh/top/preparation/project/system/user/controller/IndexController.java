package qqzsh.top.preparation.project.system.user.controller;

import java.util.List;

import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.project.content.order.domain.Order;
import qqzsh.top.preparation.project.content.order.service.IOrderService;
import qqzsh.top.preparation.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import qqzsh.top.preparation.framework.config.PreparationConfig;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.project.system.menu.domain.Menu;
import qqzsh.top.preparation.project.system.menu.service.IMenuService;
import qqzsh.top.preparation.project.system.user.domain.User;

/**
 * 首页 业务处理
 * 
 * @author zsh
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private PreparationConfig preparationConfig;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IUserDownloadService userDownloadService;

    @Autowired
    private IOrderService orderService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        // 取身份信息
        User user = getSysUser();
        // 根据用户id取出菜单
        List<Menu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", preparationConfig.getCopyrightYear());
        mmap.put("demoEnabled", preparationConfig.isDemoEnabled());
        return "index";
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap)
    {
        return "skin";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        //redis获取数据-用户数
        if (!redisUtil.hasKey("userNum")) {
            redisUtil.set("userNum", userService.selectUserList(new User()).size());
        }
        mmap.addAttribute("userNum", redisUtil.get("userNum"));

        //redis获取数据-资源数
        if (!redisUtil.hasKey("articleNums")) {
            redisUtil.set("articleNums", articleService.selectArticleList(new Article()).size());
        }
        mmap.addAttribute("articleNums", redisUtil.get("articleNums"));

        //redis获取数据-总下载数
        if (!redisUtil.hasKey("downloadNums")) {
            redisUtil.set("downloadNums", userDownloadService.selectUserDownloadList(new UserDownload()).size());
        }
        mmap.addAttribute("downloadNums", redisUtil.get("downloadNums"));

        //redis获取数据-总订单数
        if (!redisUtil.hasKey("orderNums")) {
            redisUtil.set("orderNums", orderService.selectOrderList(new Order()).size());
        }
        mmap.addAttribute("orderNums", redisUtil.get("orderNums"));

        return "main_v1";
    }
}
