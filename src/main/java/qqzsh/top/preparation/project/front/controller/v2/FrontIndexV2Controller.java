package qqzsh.top.preparation.project.front.controller.v2;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.common.utils.PageUtil;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.framework.web.controller.BaseController;
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
import qqzsh.top.preparation.project.system.post.domain.Post;
import qqzsh.top.preparation.project.system.post.service.IPostService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-03-17 9:14
 * @Description
 */
@RestController
public class FrontIndexV2Controller extends BaseController {

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
    @Autowired
    private IPostService postService;

    /**
     * 打开首页V2方法
     * @return
     */
    @GetMapping("/")
    public ModelAndView index(){
        // 获取平台数据量
        ModelAndView modelAndView = data();
        //获取友情链接
        modelAndView.addObject("allLinkList",links());
        //获取下载量最大的10条资源
        modelAndView.addObject("downloadList",getDownloadTop10());
        //获取最热10条资源
        modelAndView.addObject("hotList",getHot10());
        //获取最新10条资源
        modelAndView.addObject("newList",getNew10());
        //获取资源数最多的10个高校
        modelAndView.addObject("collegeList",getCollegeTop10());
        //获取资源数最多的10个用户
        modelAndView.addObject("userList",getUserTop10());
        modelAndView.setViewName("front/v2/index");
        return modelAndView;
    }

    /**
     * 获取最新一条通知
     * @return
     */
    public Notice getNewNotice(){
        // 获取最新一条通知
        Notice newOne = null;
        if (redisUtil.hasKey("notice")){
            newOne = (Notice) redisUtil.get("notice");
        }else {
            newOne = noticeService.getNewOne();
            redisUtil.set("notice",newOne);
        }
        return newOne;
    }

    /**
     * 获取最新一条广告
     * @return
     */
    public Notice getNewAD(){
        // 获取最新一条广告
        Notice newOneAD = null;
        if (redisUtil.hasKey("ad")){
            newOneAD = (Notice) redisUtil.get("ad");
        }else {
            newOneAD = noticeService.getNewOneAD();
            redisUtil.set("ad",newOneAD);
        }
        return newOneAD;
    }

    /**
     * 获取资源数最多的10个高校
     * @return
     */
    public List<Dept> getCollegeTop10(){
        List<Dept> depts = deptService.selectTop10();
        List<Dept> result = new ArrayList<>();
        depts.forEach(dept -> {
            int total = dept.getTotal();
            dept = deptService.selectDeptById(dept.getDeptId());
            dept.setTotal(total);
            dept.setTotalUser(deptService.selectUserCount(dept.getDeptId()).getTotalUser());
            result.add(dept);
        });
        return result;
    }

    /**
     * 获取资源数最多的10个用户
     * @return
     */
    public List<User> getUserTop10() {
        List<User> users = userService.selectTop10();
        List<User> result = new ArrayList<>();
        users.forEach(user -> {
            int total = user.getTotal();
            user = userService.selectUserById(user.getUserId());
            user.setPassword("");
            user.setSalt("");
            user.setTotal(total);
            List<Post> postByUserId = postService.findPostByUserId(user.getUserId());
            if (postByUserId.size() != 0){
                String postStr = "";
                for (int i = 0; i <postByUserId.size() ; i++) {
                    if (i == postByUserId.size()-1){
                        postStr += postByUserId.get(i).getPostName();
                    }else {
                        postStr += postByUserId.get(i).getPostName() + ",";
                    }
                }
                user.setPostStr(postStr);
            }else {
                user.setPostStr("教师");
            }
            result.add(user);
        });
        return result;
    }

    /**
     * 获取最新10条资源
     * @return
     */
    public List<Article> getNew10() {
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
        return result;
    }

    /**
     * 获取最热10条资源
     * @return
     */
    public List<Article> getHot10() {
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
        return result;
    }

    /**
     * 获取下载量最大的10条资源
     * @return
     */
    public List<Article> getDownloadTop10() {
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
        return result;
    }

    /**
     * 获取友情链接
     * @return
     */
    public List<Link> links(){
        if (redisUtil.hasKey("link_list")){
            return (List<Link>) redisUtil.get("link_list");
        }else {
            // 从数据库中查询所有友情链接并放入redis
            List<Link> links = linkService.selectLinkList(null);
            redisUtil.set("link_list",links);
            return (List<Link>) redisUtil.get("link_list");
        }
    }

    /**
     * 获取平台数据量
     * @return
     */
    public ModelAndView data(){
        ModelAndView modelAndView = new ModelAndView();
        //用户数、资源总数、下载量
        //redis获取数据-用户数
        if (!redisUtil.hasKey("userNum")) {
            redisUtil.set("userNum", userService.selectUserList(new User()).size());
        }
        modelAndView.addObject("userNum", redisUtil.get("userNum"));

        //redis获取数据-资源数
        if (!redisUtil.hasKey("articleNums")) {
            redisUtil.set("articleNums", articleService.selectArticleList(new Article()).size());
        }
        modelAndView.addObject("articleNums", redisUtil.get("articleNums"));

        //redis获取数据-总下载数
        if (!redisUtil.hasKey("downloadNums")) {
            redisUtil.set("downloadNums", userDownloadService.selectUserDownloadList(new UserDownload()).size());
        }
        modelAndView.addObject("downloadNums", redisUtil.get("downloadNums"));
        return modelAndView;
    }

    /**
     * 平台简介
     */
    @GetMapping("/front/about")
    public ModelAndView about(){
        // 获取平台数据量
        ModelAndView modelAndView = data();
        //获取友情链接
        modelAndView.addObject("allLinkList",links());
        Notice notice = new Notice();
        // 状态正常
        notice.setStatus("0");
        // 类型为平台简介
        notice.setNoticeType("3");
        startPage(1,20,"create_time desc");
        List<Notice> list = noticeService.selectNoticeList(notice);
        if (list.size() != 0){
            modelAndView.addObject("about", list.get(0).getNoticeContent());
        }else {
            modelAndView.addObject("about", "暂无简介");
        }
        modelAndView.setViewName("front/v2/about");
        return modelAndView;
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(Integer pageNum,Integer pageSize,String orderBy) {
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 新闻公告页
     */
    @GetMapping("/front/notice")
    public ModelAndView notice(@RequestParam(value = "page", required = false,defaultValue = "1") Integer page,
                               @RequestParam(value = "size", required = false,defaultValue = "10") Integer size){
        // 获取平台数据量
        ModelAndView modelAndView = data();
        //获取友情链接
        modelAndView.addObject("allLinkList",links());
        Notice notice = new Notice();
        // 状态正常
        notice.setStatus("0");
        // 类型为通知
        notice.setNoticeType("1");
        startPage(page,size,"create_time desc");
        List<Notice> list = noticeService.selectNoticeList(notice);
        // 结果集合
        modelAndView.addObject("noticeList", list);
        long total = getDataTable(list).getTotal();
        int totalPage = new Double(Math.ceil((double) total / size)).intValue();
        // 总记录数
        modelAndView.addObject("total",total);
        // 当前页
        modelAndView.addObject("page",page);
        // 页面大小
        modelAndView.addObject("size",size);
        // 总页数
        modelAndView.addObject("totalPage",totalPage);
        modelAndView.setViewName("front/v2/notice");
        return modelAndView;
    }
}
