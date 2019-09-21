package qqzsh.top.preparation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.util.StringUtil;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-21 22:09
 * @Description 用户中心-资源帖子控制器
 */
@Controller
@RequestMapping("/user/article")
public class ArticleUserController {

    @Autowired
    private ArticleService articleService;

    /**
     * 跳转到发布帖子页面
     * @return
     */
    @RequestMapping("/toPublishArticlePage")
    public ModelAndView toPublishArticlePage(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "发布帖子页面");
        mav.setViewName("user/publishArticle");
        return mav;
    }

    /**
     * 添加帖子
     * @param article
     * @param sesion
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView add(Article article, HttpSession sesion)throws Exception{
        User user=(User) sesion.getAttribute("currentUser");
        article.setPublishDate(new Date());
        article.setUser(user);
        article.setState(1);
        article.setView(StringUtil.randomInteger());
        articleService.save(article);
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "发布帖子成功页面");
        mav.setViewName("user/publishArticleSuccess");
        return mav;
    }
}

