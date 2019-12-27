package qqzsh.top.preparation.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.Notice;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.service.NoticeService;
import qqzsh.top.preparation.util.PageUtil;
import qqzsh.top.preparation.util.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 15:47
 * @description 首页或者跳转url控制器
 */
@Controller
@AllArgsConstructor
public class IndexController {

    private ArticleService articleService;
    private NoticeService noticeService;

    @Resource
    private RedisUtil<Notice> noticeRedisUtil;


    /**
     * 网站根目录请求
     *
     * @return
     */
    @RequestMapping("/")
    public ModelAndView root(HttpServletRequest request) {
        // 获取最新一条通知
        Notice newOne = null;
        if (noticeRedisUtil.hasKey("notice")){
            newOne = (Notice) noticeRedisUtil.get("notice");
        }else {
            newOne = noticeService.getNewOne();
            noticeRedisUtil.set("notice",newOne);
        }
        // 获取最新一条广告
        Notice newOneAD = null;
        if (noticeRedisUtil.hasKey("ad")){
            newOneAD = (Notice) noticeRedisUtil.get("ad");
        }else {
            newOneAD = noticeService.getNewOneAD();
            noticeRedisUtil.set("ad",newOneAD);
        }
        request.getSession().setAttribute("tMenu", "t_0");
        Article s_article = new Article();
        // 审核通过的帖子
        s_article.setState(2);
        List<Article> indexArticleList = articleService.list(s_article, 1, 20, Sort.Direction.DESC, "publishDate");
        Long total = articleService.getTotal(s_article);
        s_article.setHot(true);
        List<Article> indexHotArticleList = articleService.list(s_article, 1, 43, Sort.Direction.DESC, "publishDate");
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "首页");
        mav.addObject("newOne", newOne);
        mav.addObject("newOneAD", newOneAD);
        mav.addObject("articleList", indexArticleList);
        mav.addObject("hotArticleList", indexHotArticleList);
        mav.addObject("pageCode", PageUtil.genPagination("/article/list", total, 1, 20, ""));
        mav.setViewName("index");
        return mav;
    }

    /**
     * 打开注册页面
     *
     * @return
     */
    @GetMapping("/toRegister")
    public ModelAndView toRegister() {
        return new ModelAndView("register");
    }

    /**
     * 打开登陆页面
     *
     * @return
     */
    @GetMapping("/toLogin")
    public ModelAndView toLogin() {
        return new ModelAndView("login");
    }

    /**
     * 打开找回密码页面
     *
     * @return
     */
    @GetMapping("/findPassword")
    public ModelAndView findPassword() {
        return new ModelAndView("findPassword");
    }

    /**
     * 打开修改密码页面
     *
     * @return
     */
    @GetMapping("/modifyPassword")
    public ModelAndView modifyPassword() {
        return new ModelAndView("modifyPassword");
    }

    /**
     * 打开修改头像页面
     *
     * @return
     */
    @GetMapping("/modifyUserImage")
    public ModelAndView modifyUserImage() {
        return new ModelAndView("modifyUserImage");
    }

    /**
     * 打开管理员登陆页面
     *
     * @return
     */
    @GetMapping("/adminLogin")
    public ModelAndView adminLogin() {
        return new ModelAndView("adminLogin");
    }

    /**
     * 进入后台页面
     *
     * @param session
     * @return
     */
    @RequestMapping("/toAdmin")
    public ModelAndView toAdminPage(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "进入后台");
        //获取当前用户
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            mav.setViewName("adminLogin");
        } else if (user.getRoleName().equals("管理员")) {
            mav.setViewName("admin/adminUserCenter");
        } else {
            mav.setViewName("adminLogin");
        }
        return mav;
    }

    /**
     * 免责声明页面
     *
     * @return
     */
    @GetMapping("/tomzPage")
    public ModelAndView tomzPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", "免责声明");
        modelAndView.setViewName("mzPage");
        return modelAndView;
    }

}
