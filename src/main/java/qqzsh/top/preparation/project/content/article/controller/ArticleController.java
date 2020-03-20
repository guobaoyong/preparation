package qqzsh.top.preparation.project.content.article.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.scheduling.annotation.EnableAsync;
import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.content.change.domain.PointChange;
import qqzsh.top.preparation.project.content.change.service.IPointChangeService;
import qqzsh.top.preparation.project.content.comment.service.ICommentService;
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.project.front.lucene.ArticleIndex;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.framework.aspectj.lang.annotation.Log;
import qqzsh.top.preparation.framework.aspectj.lang.enums.BusinessType;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.comment.service.ICommentService;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;

/**
 * 资源Controller
 * 
 * @author zsh
 * @date 2019-12-30
 */
@Controller
@RequestMapping("/content/article")
@EnableAsync
public class ArticleController extends BaseController {
    private String prefix = "content/article";

    @Autowired
    private IArticleService articleService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IArcTypeService arcTypeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private ArticleIndex articleIndex;

    @Autowired
    private IUserDownloadService userDownloadService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IPointChangeService pointChangeService;

    @Autowired
    private IDeptService deptService;

    @RequiresPermissions("content:article:view")
    @GetMapping()
    public String article(ModelMap mmap) {
        //是否有审核权限
        mmap.addAttribute("ordinary",ShiroUtils.isOrdinary(ShiroUtils.getSysUser()));
        return prefix + "/article";
    }

    /**
     * 查询资源列表
     */
    @RequiresPermissions("content:article:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Article article){
        // 判断是否是普通用户
        User sysUser = ShiroUtils.getSysUser();
        if (ShiroUtils.isOrdinary(sysUser)){
            article.setArticleUserId(sysUser.getUserId());
        }
        // 判断是否是高校管理员
        if (ShiroUtils.isCollegeAdmin(sysUser)){
            article.setArticleDeptId(sysUser.getDeptId());
        }
        startPage();
        List<Article> list = articleService.selectArticleList(article);
        list.forEach(article1 -> {
            article1.setArcType(arcTypeService.selectArcTypeById(article1.getArticleTypeId()));
            article1.setUser(userService.selectUserById(article1.getArticleUserId()));
        });
        return getDataTable(list);
    }

    /**
     * 导出资源列表
     */
    @RequiresPermissions("content:article:export")
    @Log(title = "资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Article article) {
        User sysUser = ShiroUtils.getSysUser();
        if (ShiroUtils.isOrdinary(sysUser)){
            article.setArticleUserId(sysUser.getUserId());
        }
        List<Article> list = articleService.selectArticleList(article);
        ExcelUtil<Article> util = new ExcelUtil<>(Article.class);
        return util.exportExcel(list, "article");
    }

    /**
     * 新增资源
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            mmap.addAttribute("arc_type",redisUtil.get("arc_type_list"));
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            redisUtil.set("arc_type_list",arcTypes);
            mmap.addAttribute("arc_type",arcTypes);
        }
        return prefix + "/add";
    }

    /**
     * 新增保存资源
     */
    @RequiresPermissions("content:article:add")
    @Log(title = "资源", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Article article) throws Exception {
        AjaxResult ajaxResult = toAjax(articleService.insertArticle(article));
        return ajaxResult;
    }

    /**
     * 修改资源
     */
    @GetMapping("/edit/{articleId}")
    public String edit(@PathVariable("articleId") Long articleId, ModelMap mmap) {
        Article article = articleService.selectArticleById(articleId);
        mmap.put("article", article);
        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            mmap.addAttribute("arc_type",redisUtil.get("arc_type_list"));
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            redisUtil.set("arc_type_list",arcTypes);
            mmap.addAttribute("arc_type",arcTypes);
        }
        //是否有审核权限
        mmap.addAttribute("ordinary",ShiroUtils.isOrdinary(ShiroUtils.getSysUser()));
        return prefix + "/edit";
    }

    /**
     * 修改保存资源
     */
    @RequiresPermissions("content:article:edit")
    @Log(title = "资源", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Article article) {
        User sysUser = ShiroUtils.getSysUser();
        if (ShiroUtils.isOrdinary(sysUser)){
            // 普通用户更新将状态改为待审核
            article.setArticleState(0L);
            article.setArticleView(0L);
            // 从redis中删除
            try {
                redisUtil.delete("article_"+article.getArticleId());
            }catch (Exception e){ }
            // 从索引中删除
            try {
                articleIndex.deleteIndex(String.valueOf(article.getArticleId()));
            }catch (Exception e){ }
        }else {
            //判断是否是状态变更
            Article oldArticle = articleService.selectArticleById(article.getArticleId());
            // 更新时间
            article.setArticleCheckDate(new Date());
            article.setArticleView(0L);
            if (!oldArticle.getArticleState().equals(article.getArticleState())){
                Message message = new Message();
                message.setUserId(articleService.selectArticleById(article.getArticleId()).getArticleUserId());
                message.setPublishDate(new Date());
                message.setSee(0);
                // 判断状态是否是审核通过
                if (article.getArticleState().equals(1L) && (oldArticle.getArticleState().equals(0L) || oldArticle.getArticleState().equals(2L))){
                    // 消息模块添加
                    message.setContent("【审核通过】您发布的【" + article.getArticleName() + "】帖子审核成功！审核人："+ShiroUtils.getSysUser().getUserName());
                    messageService.insertMessage(message);
                    // 高校
                    article.setDept(deptService.selectDeptById(article.getArticleDeptId()));
                    // 资源类别
                    article.setArcType(arcTypeService.selectArcTypeById(article.getArticleTypeId()));
                    // 帖子加入索引
                    try {
                        articleIndex.addIndex(article);
                    }catch (Exception e){
                        articleIndex.updateIndex(article);
                    }
                } else if (article.getArticleState().equals(2L)){
                    // 消息模块添加
                    message.setContent("【审核失败】您发布的【" + article.getArticleName() + "】帖子审核未成功，原因是：" + article.getArticleReason() +"\n审核人："+ShiroUtils.getSysUser().getUserName());
                    messageService.insertMessage(message);
                }
            }
        }
        return toAjax(articleService.updateArticle(article));
    }

    /**
     * 删除资源
     */
    @RequiresPermissions("content:article:remove")
    @Log(title = "资源", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        String[] strings = Convert.toStrArray(ids);
        for (int i = 0; i < strings.length; i++) {
            //删除该帖子的下载信息
            userDownloadService.deleteByArticleId(Long.parseLong(strings[i]));
            //删除该帖子下的所有评论
            commentService.deleteByArticleId(Long.parseLong(strings[i]));
            //删除索引
            articleIndex.deleteIndex(strings[i]);
            //删除redis索引
            redisUtil.delete("article_" + strings[i]);
            //更新redis
            if (!redisUtil.hasKey("articleNums")) {
                redisUtil.set("articleNums", articleService.selectArticleList(new Article()).size());
            } else {
                int num = (int) redisUtil.get("articleNums");
                redisUtil.delete("articleNums");
                redisUtil.set("articleNums", num - 1);
            }
        }
        //删除帖子
        return toAjax(articleService.deleteArticleByIds(ids));
    }

    @Log(title = "资源", businessType = BusinessType.UPDATE)
    @RequiresPermissions("content:article:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(Article article) {
        Article articleById = articleService.selectArticleById(article.getArticleId());
        Long articleUserId = articleById.getArticleUserId();
        User userById = userService.selectUserById(articleUserId);
        PointChange pointChange = new PointChange();
        // 设为热门资源，奖励50积分
        if (article.isArticleIsHot()){
            //积分变更记录
            pointChange.setPointContent("资源【"+ articleById.getArticleName()+"】被管理员【"+ShiroUtils.getSysUser().getUserName()+"】评为优秀资源，奖励50积分！");
            pointChange.setPointFront(Long.parseLong(String.valueOf(userById.getPoint())));
            pointChange.setPointEnd(Long.parseLong(String.valueOf(userById.getPoint() + 50)));
            pointChange.setPointChange(50L);
            pointChange.setPointUserId(articleUserId);
            pointChange.setPointStatus(2);
            pointChange.setPointLoginName(userById.getLoginName());
            pointChange.setPointCreateTime(DateUtils.getNowDate());
            pointChange.setPointUpdateTime(DateUtils.getNowDate());
            pointChange.setPointSymbol("+");
            pointChangeService.insertPointChange(pointChange);
        }else {
            // 取消热门资源，扣50积分
            pointChange.setPointContent("资源【"+ articleById.getArticleName()+"】被管理员【"+ShiroUtils.getSysUser().getUserName()+"】取消优秀资源，扣50积分！");
            pointChange.setPointFront(Long.parseLong(String.valueOf(userById.getPoint())));
            pointChange.setPointEnd(Long.parseLong(String.valueOf(userById.getPoint() - 50)));
            pointChange.setPointChange(50L);
            pointChange.setPointUserId(articleUserId);
            pointChange.setPointStatus(2);
            pointChange.setPointLoginName(userById.getLoginName());
            pointChange.setPointCreateTime(DateUtils.getNowDate());
            pointChange.setPointUpdateTime(DateUtils.getNowDate());
            pointChange.setPointSymbol("-");
            if (pointChange.getPointEnd().intValue() < 0){
                pointChange.setPointEnd(0L);
            }
            pointChangeService.insertPointChange(pointChange);
        }
        // 更新用户
        userById.setPoint(Integer.parseInt(String.valueOf(pointChange.getPointEnd())));
        userService.updateUserInfo(userById);
        // 更新时间
        article.setArticleCheckDate(new Date());
        return toAjax(articleService.updateArticle(article));
    }
}
