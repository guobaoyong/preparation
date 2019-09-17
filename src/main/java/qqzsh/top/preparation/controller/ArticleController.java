package qqzsh.top.preparation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.ArcType;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.init.InitSystem;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 17:06
 * @description 资源帖子控制器
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 根据条件分页查询资源帖子信息
     * @return
     */
    @RequestMapping("/list/{id}")
    public ModelAndView list(@RequestParam(value="typeId",required=false)Integer typeId,@PathVariable(value="id",required=false)Integer page,HttpServletRequest request){
        ModelAndView mav=new ModelAndView();
        Article s_article=new Article();
        s_article.setState(2); // 审核通过的帖子
        if(typeId==null){
            mav.addObject("title", "第"+page+"页");
        }else{
            ArcType arcType = InitSystem.arcTypeMap.get(typeId);
            s_article.setArcType(arcType);
            mav.addObject("title", arcType.getName()+"-第"+page+"页");
            request.getSession().setAttribute("tMenu", "t_"+typeId);
        }
        mav.addObject("title", "第"+page+"页");
        List<Article> indexArticleList = articleService.list(s_article, page, 20, Sort.Direction.DESC,"publishDate");
        Long total=articleService.getTotal(s_article);
        mav.addObject("articleList", indexArticleList);
        s_article.setHot(true);
        mav.addObject("hotArticleList", articleService.list(s_article, 1, 43, Sort.Direction.DESC,"publishDate"));
        StringBuffer param=new StringBuffer();
        if(typeId!=null){
            param.append("?typeId="+typeId);
        }
        mav.addObject("pageCode", PageUtil.genPagination("/article/list", total, page, 20, param.toString()));
        mav.setViewName("index");
        return mav;
    }
}

