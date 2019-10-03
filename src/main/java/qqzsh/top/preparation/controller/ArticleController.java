package qqzsh.top.preparation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.ArcType;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.init.InitSystem;
import qqzsh.top.preparation.lucene.ArticleIndex;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.service.CommentService;
import qqzsh.top.preparation.util.PageUtil;
import qqzsh.top.preparation.util.RedisUtil;
import qqzsh.top.preparation.util.StringUtil;

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

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleIndex articleIndex;

    @Autowired
    private RedisUtil<Article> redisUtil;

    /**
     * 根据条件分页查询资源帖子信息
     *
     * @return
     */
    @RequestMapping("/list/{id}")
    public ModelAndView list(@RequestParam(value = "typeId", required = false) Integer typeId, @PathVariable(value = "id", required = false) Integer page, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Article s_article = new Article();
        s_article.setState(2); // 审核通过的帖子
        if (typeId == null) {
            mav.addObject("title", "第" + page + "页");
        } else {
            ArcType arcType = InitSystem.arcTypeMap.get(typeId);
            s_article.setArcType(arcType);
            mav.addObject("title", arcType.getName() + "-第" + page + "页");
            request.getSession().setAttribute("tMenu", "t_" + typeId);
        }
        mav.addObject("title", "第" + page + "页");
        List<Article> indexArticleList = articleService.list(s_article, page, 20, Sort.Direction.DESC, "publishDate");
        Long total = articleService.getTotal(s_article);
        mav.addObject("articleList", indexArticleList);
        s_article.setHot(true);
        mav.addObject("hotArticleList", articleService.list(s_article, 1, 43, Sort.Direction.DESC, "publishDate"));
        StringBuffer param = new StringBuffer();
        if (typeId != null) {
            param.append("?typeId=" + typeId);
        }
        mav.addObject("pageCode", PageUtil.genPagination("/article/list", total, page, 20, param.toString()));
        mav.setViewName("index");
        return mav;
    }

    /**
     * 根据id查询帖子详细信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable("id") Integer id) throws Exception {
        ModelAndView mav = new ModelAndView();
        Article article = null;
        String key = "article_" + id;
        if (redisUtil.hasKey(key)) {
            article = (Article) redisUtil.get(key);
        } else {
            article = articleService.get(id);
            redisUtil.set(key, article, 60 * 60);
        }
        //查看次数由数据库获取
        article.setView(articleService.get(id).getView());
        mav.addObject("article", article);
        mav.addObject("title", article.getName());

        List<Article> hotArticleList = null;
        String hKey = "hotArticleList_type_" + article.getArcType().getId();
        if (redisUtil.hasKey(hKey)) {
            hotArticleList = redisUtil.lGet(hKey, 0, -1);
        } else {
            Article s_article = new Article();
            s_article.setHot(true);
            s_article.setArcType(article.getArcType());
            hotArticleList = articleService.list(s_article, 1, 43, Sort.Direction.DESC, "publishDate");
            redisUtil.lSet(hKey, hotArticleList, 60 * 60);
        }
        mav.addObject("hotArticleList", hotArticleList);
        Comment s_comment = new Comment();
        s_comment.setArticle(article);
        // 审核通过的评论信息
        s_comment.setState(1);
        mav.addObject("commentCount", commentService.getTotal(s_comment));
        mav.setViewName("article");
        return mav;
    }

    /**
     * 关键字分词搜索
     *
     * @return
     */
    @RequestMapping("/search")
    public ModelAndView search(String q, @RequestParam(value = "page", required = false) String page, HttpServletRequest request) throws Exception {
        request.getSession().setAttribute("tMenu", "t_0");
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        Article s_article = new Article();
        s_article.setHot(true);
        List<Article> hotArticleList = articleService.list(s_article, 1, 43, Sort.Direction.DESC, "publishDate");
        List<Article> articleList = articleIndex.search(q);
        Integer toIndex = articleList.size() >= Integer.parseInt(page) * 10 ? Integer.parseInt(page) * 10 : articleList.size();
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", q);
        mav.addObject("q", q);
        mav.addObject("articleList", articleList.subList((Integer.parseInt(page) - 1) * 10, toIndex));
        mav.addObject("resultTotal", articleList.size());
        mav.addObject("hotArticleList", hotArticleList);
        mav.addObject("pageCode", this.genUpAndDownPageCode(Integer.parseInt(page), articleList.size(), q, 10));
        mav.setViewName("result");
        return mav;
    }

    /**
     * 生成上一页，下一页代码
     *
     * @param page
     * @param totalNum
     * @param q
     * @param pageSize
     * @return
     */
    private String genUpAndDownPageCode(Integer page, Integer totalNum, String q, Integer pageSize) {
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        StringBuffer pageCode = new StringBuffer();
        if (totalPage == 0) {
            return "";
        } else {
            pageCode.append("<div class='layui-box layui-laypage layui-laypage-default'>");
            if (page > 1) {
                pageCode.append("<a href='/article/search?page=" + (page - 1) + "&q=" + q + "' class='layui-laypage-prev'>上一页</a>");
            } else {
                pageCode.append("<a href='#' class='layui-laypage-prev layui-disabled'>上一页</a>");
            }
            if (page < totalPage) {
                pageCode.append("<a href='/article/search?page=" + (page + 1) + "&q=" + q + "' class='layui-laypage-next'>下一页</a>");
            } else {
                pageCode.append("<a href='#' class='layui-laypage-next layui-disabled'>下一页</a>");
            }
            pageCode.append("</div>");
        }
        return pageCode.toString();
    }

    /**
     * 加载相关资源
     *
     * @param q
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/loadRelatedResources")
    public List<Article> loadRelatedResources(String q) throws Exception {
        if (StringUtil.isEmpty(q)) {
            return null;
        }
        List<Article> articleList = articleIndex.searchNoHighLighter(q);
        return articleList;
    }

    /**
     * 查看次数加1
     *
     * @param id
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/updateView")
    public void updateView(Integer id) throws Exception {
        Article article = articleService.get(id);
        article.setView(article.getView() + 1);
        articleService.save(article);
    }
}

