package qqzsh.top.preparation.project.front.controller.v2;

import com.github.pagehelper.PageHelper;
import org.htmlparser.Parser;
import org.htmlparser.visitors.TextExtractingVisitor;
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
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.project.front.lucene.ArticleIndex;
import qqzsh.top.preparation.project.system.dept.domain.Dept;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-03-18 22:03
 * @Description
 */
@RestController
@RequestMapping("/front")
public class ArticleV2Controller extends BaseController {

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
    private IArcTypeService arcTypeService;
    @Autowired
    private ArticleIndex articleIndex;

    /**
     * 学科资源页面
     * @return
     */
    @GetMapping("/article")
    public ModelAndView article(@RequestParam(value = "page", required = false,defaultValue = "1") Integer page,
                                @RequestParam(value = "size", required = false,defaultValue = "10") Integer size,
                                @RequestParam(value = "deptName", required = false,defaultValue = "") String deptName,
                                @RequestParam(value = "arcType", required = false,defaultValue = "") String arcType,
                                @RequestParam(value = "q", required = false,defaultValue = "") String q) throws Exception {
        // 获取平台数据量
        ModelAndView modelAndView = data();
        //获取友情链接
        modelAndView.addObject("allLinkList",links());
        // 获取资源类别
        modelAndView.addObject("arcTypesList",arcTypes());
        // 获取所有高校
        modelAndView.addObject("deptsList",depts());

        List<Article> result = new ArrayList<>();

        long total =  0;
        int totalPage = 0;

        // 分情况 q为空时
        if (StringUtils.isBlank(q)){
            // 调用数据库进行搜索
            Article article = new Article();
            article.setArticleState(1L);
            if (StringUtils.isNotBlank(deptName)){
                article.setArticleDeptId(Long.parseLong(deptName));
            }
            if (StringUtils.isNotBlank(arcType)){
                article.setArticleTypeId(Long.parseLong(arcType));
            }
            startPage(page,size,"article_publish_date desc");
            result = articleService.selectArticleList(article);
            result.forEach(r -> {
                //用户名
                r.setUser(userService.selectUserById(r.getArticleUserId()));
                //资源类别
                r.setArcType(arcTypeService.selectArcTypeById(r.getArticleTypeId()));
                //高校
                r.setDept(deptService.selectDeptById(r.getArticleDeptId()));
                r.setArticleContent("");
                r.setArticleDownload1("");
                r.setArticlePassword1("");
                // 下载量
                r.setTotal(articleService.selectDownloadCount(r.getArticleId()));
            });
            total = getDataTable(result).getTotal();
            totalPage = new Double(Math.ceil((double) total / size)).intValue();
            // 摘要
            result.forEach(r -> {
                String content = articleService.selectArticleById(r.getArticleId()).getArticleContent();
                try {
                    Parser parser = new Parser(content);
                    TextExtractingVisitor visitor = new TextExtractingVisitor();
                    parser.visitAllNodesWith(visitor);
                    content = visitor.getExtractedText().replaceAll("\\s*|\t|\r|\n","");
                } catch (Exception e) {
                    content = "暂无摘要";
                }
                content = content.length() > 150 ? content.substring(0,151) + "..." : content;
                r.setArticleContent(content);
            });
        }else {
            String tempDeptName = "";
            String tempArcType = "";
            if (StringUtils.isNotBlank(deptName)){
                tempDeptName = deptService.selectDeptById(Long.parseLong(deptName)).getDeptName();
            }
            if (StringUtils.isNotBlank(arcType)){
                tempArcType = arcTypeService.selectArcTypeById(Long.parseLong(arcType)).getSrcTypeName();
            }
            // 调用索引查询
            List<Article> articleList = articleIndex.search(q,tempDeptName,tempArcType);
            Integer toIndex = articleList.size() >= page * size ? page * size : articleList.size();
            result = articleList.subList((page - 1) * size, toIndex);
            result.forEach(r -> {
                Article article = articleService.selectArticleById(r.getArticleId());
                //用户名
                r.setUser(userService.selectUserById(article.getArticleUserId()));
                //资源类别
                r.setArcType(arcTypeService.selectArcTypeById(article.getArticleTypeId()));
                //高校
                r.setDept(deptService.selectDeptById(article.getArticleDeptId()));
                r.setArticleDownload1("");
                r.setArticlePassword1("");
                // 下载量
                r.setTotal(articleService.selectDownloadCount(article.getArticleId()));
                // 浏览量
                r.setArticleView(article.getArticleView());
            });
            total = articleList.size();
            totalPage = new Double(Math.ceil((double) total / size)).intValue();
        }
        // 结果集合
        modelAndView.addObject("resultList",result);
        // 总记录数
        modelAndView.addObject("total",total);
        // 当前页
        modelAndView.addObject("page",page);
        // 页面大小
        modelAndView.addObject("size",size);
        // 总页数
        modelAndView.addObject("totalPage",totalPage);
        // 返回前台选中高校
        modelAndView.addObject("deptName",deptName);
        if (StringUtils.isNotBlank(deptName)){
            modelAndView.addObject("deptNameStr",deptService.selectDeptById(Long.parseLong(deptName)).getDeptName());
        }else {
            modelAndView.addObject("deptNameStr","");
        }
        // 返回前台选中类别
        modelAndView.addObject("arcType",arcType);
        if (StringUtils.isNotBlank(arcType)){
            modelAndView.addObject("arcTypeStr",arcTypeService.selectArcTypeById(Long.parseLong(arcType)).getSrcTypeName());
        }else {
            modelAndView.addObject("arcTypeStr","");
        }
        // 返回前台关键词
        modelAndView.addObject("q",q);
        modelAndView.setViewName("front/v2/article");
        return modelAndView;
    }

    /**
     * 获取所有高校
     */
    public List<Dept> depts(){
        Dept dept = new Dept();
        // 顶层高校
        dept.setParentId(100L);
        List<Dept> depts = deptService.selectDeptList(dept);
        depts.forEach(d -> {
            Article article1 = new Article();
            article1.setArticleState(1L);
            article1.setArticleDeptId(d.getDeptId());
            d.setTotal(articleService.selectArticleListCount(article1));
        });
        return depts;
    }

    /**
     * 获取所有资源类别
     */
    public List<ArcType> arcTypes(){
        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            //每个资源类别的数量
            List<ArcType> arc_type_list = (List<ArcType>) redisUtil.get("arc_type_list");
            return arc_type_list;
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            arcTypes.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                arcType.setAllSize(articleService.selectArticleListCount(article1));
            });
            redisUtil.set("arc_type_list",arcTypes);
            return arcTypes;
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

    /**
     * 设置请求分页数据
     */
    protected void startPage(Integer pageNum,Integer pageSize,String orderBy) {
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
}
