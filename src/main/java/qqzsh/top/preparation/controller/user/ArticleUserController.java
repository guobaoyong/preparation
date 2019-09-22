package qqzsh.top.preparation.controller.user;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.util.DateUtil;
import qqzsh.top.preparation.util.StringUtil;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${articleImageFilePath}")
    private String articleImageFilePath;

    /**
     * Layui编辑器图片上传处理
     * @param file 待上传文件
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadImage")
    public Map<String,Object> uploadImage(MultipartFile file)throws Exception{
        Map<String,Object> map=new HashMap<>();
        if(!file.isEmpty()){
            String fileName=file.getOriginalFilename(); // 获取文件名
            String suffixName=fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
            String newFileName= DateUtil.getCurrentDateStr()+suffixName; // 新文件名
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(articleImageFilePath+DateUtil.getCurrentDatePath()+newFileName));
            map.put("code", 0);
            map.put("msg", "上传成功");
            Map<String,Object> map2=new HashMap<>();
            map2.put("src", "/image/"+DateUtil.getCurrentDatePath()+newFileName);
            map2.put("title", newFileName);
            map.put("data", map2);
        }
        return map;
    }

    /**
     * 根据条件分页查询资源帖子信息
     * @param s_article
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/list")
    public Map<String,Object> list(Article s_article, HttpSession session, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="limit",required=false)Integer limit)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        User user=(User) session.getAttribute("currentUser");
        s_article.setUser(user);
        List<Article> articleList = articleService.list(s_article, page, limit, Sort.Direction.DESC, "publishDate");
        Long count = articleService.getTotal(s_article);
        resultMap.put("code", 0);
        resultMap.put("count", count);
        resultMap.put("data", articleList);
        return resultMap;
    }

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
     * 跳转到帖子管理页面
     * @return
     */
    @RequestMapping("/toArticleManagePage")
    public ModelAndView toArticleManagePage(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "帖子管理");
        mav.setViewName("user/articleManage");
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

