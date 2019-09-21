package qqzsh.top.preparation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.ArticleService;

import javax.servlet.http.HttpSession;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-21 21:43
 * @Description 用户页面跳转控制器
 */
@Controller
public class IndexUserController {

    @Autowired
    private ArticleService articleService;

    /**
     * 跳转用后中心页面
     * @return
     */
    @RequestMapping("/toUserCenterPage")
    public ModelAndView toUserCenterPage(HttpSession session){
        User user=(User) session.getAttribute("currentUser");
        Article s_article=new Article();
        s_article.setUser(user);
        s_article.setUseful(false);;
        Long total = articleService.getTotal(s_article);
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "用户中心页面");
        //失效链接数目
        session.setAttribute("unUserfulArticleCount", total);
        mav.setViewName("user/userCenter");
        return mav;
    }
}

