package qqzsh.top.preparation.project.front.controller.v2;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
import qqzsh.top.preparation.project.system.dept.controller.DeptController;
import qqzsh.top.preparation.project.system.dept.domain.Dept;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.notice.domain.Notice;
import qqzsh.top.preparation.project.system.notice.service.INoticeService;
import qqzsh.top.preparation.project.system.post.service.IPostService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import java.util.List;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-03-17 17:11
 * @Description
 */
@RestController
@RequestMapping("/front")
public class CollegeController extends BaseController {

    @Autowired
    private IDeptService deptService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ILinkService linkService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private IUserDownloadService userDownloadService;
    @Autowired
    private INoticeService noticeService;

    /**
     * 高校页面
     * @return
     */
    @GetMapping("/college")
    public ModelAndView college(@RequestParam(value = "page", required = false,defaultValue = "1") Integer page,
                                @RequestParam(value = "size", required = false,defaultValue = "3") Integer size){
        // 获取平台数据量
        ModelAndView modelAndView = data();
        //获取友情链接
        modelAndView.addObject("allLinkList",links());
        //分页获取高校
        startPage(page,size,"order_num asc");
        Dept dept = new Dept();
        // 顶层高校
        dept.setParentId(100L);
        List<Dept> depts = deptService.selectDeptList(dept);
        // 高校中英文名、校徽、资源总数、注册用户数、官网
        depts.forEach(tempDept -> {
            tempDept.setTotalUser(deptService.selectUserCount(tempDept.getDeptId()).getTotalUser());
            Article article = new Article();
            article.setArticleDeptId(tempDept.getDeptId());
            tempDept.setTotal(articleService.selectArticleListCount(article));
        });
        long total = getDataTable(depts).getTotal();
        int totalPage = new Double(Math.ceil((double) total / size)).intValue();
        // 结果集合
        modelAndView.addObject("deptsList",depts);
        // 总记录数
        modelAndView.addObject("total",total);
        // 当前页
        modelAndView.addObject("page",page);
        // 页面大小
        modelAndView.addObject("size",size);
        // 总页数
        modelAndView.addObject("totalPage",totalPage);
        //获取最新通知
        modelAndView.addObject("newOneNotice",getNewNotice());
        modelAndView.setViewName("front/v2/college");
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
     * 设置请求分页数据
     */
    protected void startPage(Integer pageNum,Integer pageSize,String orderBy) {
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
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
}
