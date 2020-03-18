package qqzsh.top.preparation.project.front.controller.v2;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.project.content.link.domain.Link;
import qqzsh.top.preparation.project.content.link.service.ILinkService;
import qqzsh.top.preparation.project.system.dept.domain.Dept;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import java.util.*;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-03-18 18:20
 * @Description
 */
@RestController
@RequestMapping("/front")
public class ScholarController extends BaseController {

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

    /**
     * 教师学者页面
     * @return
     */
    @GetMapping("/scholar")
    public ModelAndView scholar(@RequestParam(value = "page", required = false,defaultValue = "1") Integer page,
                                @RequestParam(value = "size", required = false,defaultValue = "5") Integer size,
                                @RequestParam(value = "deptId", required = false,defaultValue = "") Long deptId,
                                @RequestParam(value = "name", required = false,defaultValue = "") String name){
        // 获取平台数据量
        ModelAndView modelAndView = data();
        //获取友情链接
        modelAndView.addObject("allLinkList",links());
        // 先获取学者再获取高校
        startPage(page,size,"create_time desc");
        User user = new User();
        // 搜索条件 高校+学者名称
        if (deptId != null){
            user.setDeptId(deptId);
        }
        if (StringUtils.isNotBlank(String.valueOf(name))){
            user.setUserName(name);
        }
        List<User> users = userService.selectUserList(user);
        users.forEach(u -> {
            u.setDept(deptService.selectDeptById(u.getDeptId()));
            // 计算资源量
            Article article = new Article();
            article.setArticleUserId(u.getUserId());
            u.setTotal(articleService.selectArticleListCount(article));
        });
        // 分情况
        // 1.高校ID为空，搜索内容不为空
        List<Dept> depts = new ArrayList<>();
        if (deptId == null &&  StringUtils.isNotBlank(name)){
            // 高校由教师提供
            List<Dept> temp = new ArrayList<>();
            users.forEach(u -> {
                temp.add(deptService.selectDeptById(u.getDeptId()));
            });
            ArrayList<Dept> resultDepts = removeDuplicteDepts(temp);
            resultDepts.forEach(result -> {
                users.forEach(u -> {
                    if (result.getDeptId().equals(u.getDeptId())){
                        result.setTotalUser(result.getTotalUser() + 1);
                    }
                });
            });
            modelAndView.addObject("deptsList",resultDepts);
        }
        // 情况2 高校ID不为空，name不为空
        else if (deptId != null &&  StringUtils.isNotBlank(name)){
            // 高校由高校ID查询得出
            List<Dept> temp = new ArrayList<>();
            temp.add(deptService.selectDeptById(deptId));
            temp.forEach(result -> {
                result.setTotalUser(users.size());
            });
            modelAndView.addObject("deptsList",temp);
        }
        // 情况3 只高校ID为空
        else if (deptId == null){
            Dept dept = new Dept();
            // 顶层高校
            dept.setParentId(100L);
            // 所有高校及注册用户数
            depts = deptService.selectDeptList(dept);
            // 高校注册用户数
            depts.forEach(tempDept -> {
                tempDept.setTotalUser(deptService.selectUserCount(tempDept.getDeptId()).getTotalUser());
            });
            modelAndView.addObject("deptsList",depts);
        }else {
            // 情况4 只显示一个高校
            depts.add(deptService.selectDeptById(deptId));
            // 高校注册用户数
            depts.forEach(tempDept -> {
                tempDept.setTotalUser(deptService.selectUserCount(tempDept.getDeptId()).getTotalUser());
            });
            modelAndView.addObject("deptsList",depts);
        }
        // 高校集合
        long total = getDataTable(users).getTotal();
        int totalPage = new Double(Math.ceil((double) total / size)).intValue();
        // 结果集合
        modelAndView.addObject("usersList",users);
        // 总记录数
        modelAndView.addObject("total",total);
        // 当前页
        modelAndView.addObject("page",page);
        // 页面大小
        modelAndView.addObject("size",size);
        // 总页数
        modelAndView.addObject("totalPage",totalPage);
        modelAndView.setViewName("front/v2/scholars");
        // 学者姓名、高校ID返回前台
        modelAndView.addObject("name",name);
        modelAndView.addObject("deptId",deptId);
        return modelAndView;
    }

    /**
     * 将列表中重复的高校移除，重复指的是name相同
     * @param deptList
     * @return
     */
    public ArrayList<Dept> removeDuplicteDepts(List<Dept> deptList) {
        Set<Dept> s = new TreeSet<Dept>(new Comparator<Dept>() {
            @Override
            public int compare(Dept o1, Dept o2) {
                return o1.getDeptName().compareTo(o2.getDeptName());
            }
        });
        s.addAll(deptList);
        return new ArrayList<Dept>(s);
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
