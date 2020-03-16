package qqzsh.top.preparation.project.front.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.project.content.link.domain.Link;
import qqzsh.top.preparation.project.content.link.service.ILinkService;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.project.system.dept.domain.Dept;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.notice.domain.Notice;
import qqzsh.top.preparation.project.system.notice.service.INoticeService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-03-16 13:33
 * @Description 首页接口提供
 */
@RestController
@RequestMapping("/front")
public class FIndexController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private INoticeService noticeService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IArcTypeService arcTypeService;
    @Autowired
    private ILinkService linkService;
    @Autowired
    private IUserDownloadService userDownloadService;

    /**
     * 获取最新一条通知
     * @return
     */
    @GetMapping("/getNewNotice")
    public AjaxResult getNewNotice(){
        // 获取最新一条通知
        Notice newOne = null;
        if (redisUtil.hasKey("notice")){
            newOne = (Notice) redisUtil.get("notice");
        }else {
            newOne = noticeService.getNewOne();
            redisUtil.set("notice",newOne);
        }
        return AjaxResult.success(newOne);
    }

    /**
     * 获取最新一条广告
     * @return
     */
    @GetMapping("/getNewAD")
    public AjaxResult getNewAD(){
        // 获取最新一条广告
        Notice newOneAD = null;
        if (redisUtil.hasKey("ad")){
            newOneAD = (Notice) redisUtil.get("ad");
        }else {
            newOneAD = noticeService.getNewOneAD();
            redisUtil.set("ad",newOneAD);
        }
        return AjaxResult.success(newOneAD);
    }

    /**
     * 获取资源数最多的10个高校
     * @return
     */
    @GetMapping("/getCollegeTop10")
    public AjaxResult getCollegeTop10(){
        List<Dept> depts = deptService.selectTop10();
        List<Dept> result = new ArrayList<>();
        depts.forEach(dept -> {
            int total = dept.getTotal();
            dept = deptService.selectDeptById(dept.getDeptId());
            dept.setTotal(total);
            result.add(dept);
        });
        return AjaxResult.success(result);
    }

    /**
     * 获取资源数最多的10个用户
     * @return
     */
    @GetMapping("/getUserTop10")
    public AjaxResult getUserTop10() {
        List<User> users = userService.selectTop10();
        List<User> result = new ArrayList<>();
        users.forEach(user -> {
            int total = user.getTotal();
            user = userService.selectUserById(user.getUserId());
            user.setPassword("");
            user.setSalt("");
            user.setTotal(total);
            result.add(user);
        });
        return AjaxResult.success(result);
    }

    /**
     * 获取最新10条资源
     * @return
     */
    @GetMapping("/getNew10")
    public AjaxResult getNew10() {
        List<Article> articles = articleService.selectNew10();
        List<Article> result = new ArrayList<>();
        articles.forEach(article -> {
            //用户名
            article.setUser(userService.selectUserById(article.getArticleUserId()));
            //资源类别
            article.setArcType(arcTypeService.selectArcTypeById(article.getArticleTypeId()));
            //高校
            article.setDept(deptService.selectDeptById(article.getArticleDeptId()));
            article.setArticleContent("");
            article.setArticleDownload1("");
            article.setArticlePassword1("");
            result.add(article);
        });
        return AjaxResult.success(result);
    }

    /**
     * 获取最热10条资源
     * @return
     */
    @GetMapping("/getHot10")
    public AjaxResult getHot10() {
        List<Article> articles = articleService.selectHot10();
        List<Article> result = new ArrayList<>();
        articles.forEach(article -> {
            //用户名
            article.setUser(userService.selectUserById(article.getArticleUserId()));
            //资源类别
            article.setArcType(arcTypeService.selectArcTypeById(article.getArticleTypeId()));
            //高校
            article.setDept(deptService.selectDeptById(article.getArticleDeptId()));
            article.setArticleContent("");
            article.setArticleDownload1("");
            article.setArticlePassword1("");
            result.add(article);
        });
        return AjaxResult.success(result);
    }

    /**
     * 获取下载量最大的10条资源
     * @return
     */
    @GetMapping("/getDownloadTop10")
    public AjaxResult getDownloadTop10() {
        List<Article> articles = articleService.selectDownloadTop10();
        List<Article> result = new ArrayList<>();
        articles.forEach(article -> {
            int total = article.getTotal();
            article = articleService.selectArticleById(article.getArticleId());
            article.setTotal(total);
            //用户名
            article.setUser(userService.selectUserById(article.getArticleUserId()));
            //资源类别
            article.setArcType(arcTypeService.selectArcTypeById(article.getArticleTypeId()));
            //高校
            article.setDept(deptService.selectDeptById(article.getArticleDeptId()));
            article.setArticleContent("");
            article.setArticleDownload1("");
            article.setArticlePassword1("");
            result.add(article);
        });
        return AjaxResult.success(result);
    }

    /**
     * 获取友情链接
     * @return
     */
    @GetMapping("/links")
    public AjaxResult links(){
        if (redisUtil.hasKey("link_list")){
            return AjaxResult.success(redisUtil.get("link_list"));
        }else {
            // 从数据库中查询所有友情链接并放入redis
            List<Link> links = linkService.selectLinkList(null);
            redisUtil.set("link_list",links);
            return AjaxResult.success(redisUtil.get("link_list"));
        }
    }

    /**
     * 获取平台数据量
     * @return
     */
    @GetMapping("/data")
    public AjaxResult data(){
        JSONObject jsonObject = new JSONObject();
        //用户数、资源总数、下载量
        //redis获取数据-用户数
        if (!redisUtil.hasKey("userNum")) {
            redisUtil.set("userNum", userService.selectUserList(new User()).size());
        }
        jsonObject.put("userNum", redisUtil.get("userNum"));

        //redis获取数据-资源数
        if (!redisUtil.hasKey("articleNums")) {
            redisUtil.set("articleNums", articleService.selectArticleList(new Article()).size());
        }
        jsonObject.put("articleNums", redisUtil.get("articleNums"));

        //redis获取数据-总下载数
        if (!redisUtil.hasKey("downloadNums")) {
            redisUtil.set("downloadNums", userDownloadService.selectUserDownloadList(new UserDownload()).size());
        }
        jsonObject.put("downloadNums", redisUtil.get("downloadNums"));
        return AjaxResult.success(jsonObject);
    }

    @GetMapping("/test")
    public AjaxResult test(){
        List<Article> articles = articleService.selectDownloadTop10();
        List<Article> result = new ArrayList<>();
        articles.forEach(article -> {
            int total = article.getTotal();
            article = articleService.selectArticleById(article.getArticleId());
            article.setTotal(total);
            //用户名
            article.setUser(userService.selectUserById(article.getArticleUserId()));
            //资源类别
            article.setArcType(arcTypeService.selectArcTypeById(article.getArticleTypeId()));
            //高校
            article.setDept(deptService.selectDeptById(article.getArticleDeptId()));
            article.setArticleContent("");
            article.setArticleDownload1("");
            article.setArticlePassword1("");
            result.add(article);
        });
        return AjaxResult.success(result);
    }
}
