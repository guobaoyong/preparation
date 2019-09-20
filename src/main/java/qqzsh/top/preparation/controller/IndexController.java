package qqzsh.top.preparation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 15:47
 * @description 首页或者跳转url控制器
 */
@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;


    /**
     * 网站根目录请求
     * @return
     */
    @RequestMapping("/")
    public ModelAndView root(HttpServletRequest request){
        request.getSession().setAttribute("tMenu", "t_0");
        Article s_article=new Article();
        s_article.setState(2); // 审核通过的帖子
        List<Article> indexArticleList = articleService.list(s_article, 1, 20, Sort.Direction.DESC,"publishDate");
        Long total=articleService.getTotal(s_article);
        s_article.setHot(true);
        List<Article> indexHotArticleList = articleService.list(s_article, 1, 43, Sort.Direction.DESC,"publishDate");
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "首页");
        mav.addObject("articleList", indexArticleList);
        mav.addObject("hotArticleList", indexHotArticleList);
        mav.addObject("pageCode", PageUtil.genPagination("/article/list", total, 1, 20, ""));
        mav.setViewName("index");
        return mav;
    }

    /**
     * 打开注册页面
     * @return
     */
    @GetMapping("/toRegister")
    public ModelAndView toRegister(){
        return new ModelAndView("register");
    }

    /**
     * 打开登陆页面
     * @return
     */
    @GetMapping("/toLogin")
    public ModelAndView toLogin(){
        return new ModelAndView("login");
    }

    /**
     * 打开找回密码页面
     * @return
     */
    @GetMapping("/findPassword")
    public ModelAndView findPassword(){
        return new ModelAndView("findPassword");
    }

    /**
     * 打开修改密码页面
     * @return
     */
    @GetMapping("/modifyPassword")
    public ModelAndView modifyPassword(){
        return new ModelAndView("modifyPassword");
    }

    /**
     * 打开修改头像页面
     * @return
     */
    @GetMapping("/modifyUserImage")
    public ModelAndView modifyUserImage(){
        return new ModelAndView("modifyUserImage");
    }

}
