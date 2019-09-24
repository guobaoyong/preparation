package qqzsh.top.preparation.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.service.ArticleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-24 21:09
 * @Description 管理员-资源控制器
 */
@Controller
@RequestMapping("/admin/article")
public class ArticleAdminController {

    @Autowired
    private ArticleService articleService;

    /**
     * 分页查询资源帖子信息
     * @param s_article
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value={"分页查询资源帖子信息"})
    @RequestMapping("/list")
    public Map<String,Object> list(Article s_article, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="limit",required=false)Integer limit)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        List<Article> articleList = articleService.list(s_article,page, limit, Sort.Direction.DESC, "publishDate");
        Long total = articleService.getTotal(s_article);
        resultMap.put("code", 0);
        resultMap.put("count", total);
        resultMap.put("data", articleList);
        return resultMap;
    }
}

