package qqzsh.top.preparation.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.service.OrderService;
import qqzsh.top.preparation.service.UserDownloadService;
import qqzsh.top.preparation.service.UserService;
import qqzsh.top.preparation.util.RedisUtil;

import javax.annotation.Resource;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-22 15:01
 * @description 首页或者跳转url控制器
 */
@Controller
public class IndexAdminController {

    @Resource
    private RedisUtil<Integer> redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserDownloadService downloadService;

    @Autowired
    private OrderService orderService;

    /**
     * 跳转到管理员主页面
     *
     * @return
     */
    @RequiresPermissions(value = {"进入管理员主页"})
    @RequestMapping("/toAdminUserCenterPage")
    public ModelAndView toAdminUserCenterPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "管理员主页页面");
        mav.setViewName("admin/adminUserCenter");
        return mav;
    }

    @RequiresPermissions(value = {"管理员默认页"})
    @RequestMapping("/admin/default")
    public ModelAndView toDefaultPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "管理员主页页面");
        //redis获取数据-用户数
        if (!redisUtil.hasKey("userNum")) {
            redisUtil.set("userNum", userService.getTotal(null).intValue());
        }
        mav.addObject("userNum", redisUtil.get("userNum"));

        //redis获取数据-资源数
        if (!redisUtil.hasKey("articleNums")) {
            redisUtil.set("articleNums", articleService.getTotal(null).intValue());
        }
        mav.addObject("articleNums", redisUtil.get("articleNums"));

        //redis获取数据-总下载数
        if (!redisUtil.hasKey("downloadNums")) {
            redisUtil.set("downloadNums", downloadService.getTotal(null).intValue());
        }
        mav.addObject("downloadNums", redisUtil.get("downloadNums"));

        //redis获取数据-总订单数
        if (!redisUtil.hasKey("orderNums")) {
            redisUtil.set("orderNums", orderService.getTotal(null).intValue());
        }
        mav.addObject("orderNums", redisUtil.get("orderNums"));

        mav.setViewName("admin/default");
        return mav;
    }

    @RequiresPermissions(value = {"资源类别页"})
    @RequestMapping("/admin/arcTypeManage")
    public ModelAndView toarcTypeManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "资源类别页面");
        mav.setViewName("admin/arcTypeManage");
        return mav;
    }

    @RequiresPermissions(value = {"资源类别添加页"})
    @RequestMapping("/admin/saveArcType")
    public ModelAndView tosaveArcTypePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "资源类别添加页面");
        mav.setViewName("admin/saveArcType");
        return mav;
    }

    @RequiresPermissions(value = {"友情链接页"})
    @RequestMapping("/admin/linkManage")
    public ModelAndView tolinkManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "友情链接管理页面");
        mav.setViewName("admin/linkManage");
        return mav;
    }

    @RequiresPermissions(value = {"友情链接添加页"})
    @RequestMapping("/admin/saveLink")
    public ModelAndView tosaveLinkPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "友情链接添加页面");
        mav.setViewName("admin/saveLink");
        return mav;
    }


    @RequiresPermissions(value = {"用户管理页"})
    @RequestMapping("/admin/userManage")
    public ModelAndView touserManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "用户管理页面");
        mav.setViewName("admin/userManage");
        return mav;
    }

    @RequiresPermissions(value = {"积分充值页"})
    @RequestMapping("/admin/addPoints")
    public ModelAndView toaddPointsPage(Integer id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "用户管理页面");
        mav.addObject("id", id);
        mav.setViewName("admin/addPoints");
        return mav;
    }


    @RequiresPermissions(value = {"资源管理页"})
    @RequestMapping("/admin/articleManage")
    public ModelAndView toarticleManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "资源管理页面");
        mav.setViewName("admin/articleManage");
        return mav;
    }

    @RequiresPermissions(value = {"评论管理页"})
    @RequestMapping("/admin/commentManage")
    public ModelAndView tocommentManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "评论管理页面");
        mav.setViewName("admin/commentManage");
        return mav;
    }


    @RequiresPermissions(value = {"发送消息页"})
    @GetMapping("/admin/sentMessage")
    public ModelAndView tosentMessagePage(Integer id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "发送消息页面");
        mav.addObject("id", id);
        mav.setViewName("admin/sentMessage");
        return mav;
    }

    @RequiresPermissions(value = {"已下载管理页"})
    @RequestMapping("/admin/downloadManage")
    public ModelAndView todownloadManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "已下载管理页面");
        mav.setViewName("admin/downloadManage");
        return mav;
    }

    @RequiresPermissions(value = {"消息管理页"})
    @GetMapping("/admin/messageManage")
    public ModelAndView tomessageManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "消息管理页面");
        mav.setViewName("admin/messageManage");
        return mav;
    }

    @RequiresPermissions(value = {"订单管理页"})
    @GetMapping("/admin/orderManage")
    public ModelAndView toorderManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "订单管理页面");
        mav.setViewName("admin/orderManage");
        return mav;
    }

    @RequiresPermissions(value = {"通知广告管理页"})
    @GetMapping("/admin/noticeManage")
    public ModelAndView tonoticeManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "通知广告管理页面");
        mav.setViewName("admin/noticeManage");
        return mav;
    }

}

