package qqzsh.top.preparation.project.content.comment.controller;

import java.util.LinkedList;
import java.util.List;

import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
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
import qqzsh.top.preparation.project.content.comment.domain.Comment;
import qqzsh.top.preparation.project.content.comment.service.ICommentService;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.poi.ExcelUtil;
import qqzsh.top.preparation.framework.web.page.TableDataInfo;
import qqzsh.top.preparation.project.content.article.service.IArticleService;

/**
 * 评论Controller
 * 
 * @author zsh
 * @date 2019-12-31
 */
@Controller
@RequestMapping("/content/comment")
public class CommentController extends BaseController {

    private String prefix = "content/comment";

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;

    @RequiresPermissions("content:comment:view")
    @GetMapping()
    public String comment()
    {
        return prefix + "/comment";
    }

    /**
     * 查询评论列表
     */
    @RequiresPermissions("content:comment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Comment comment) {
        startPage();
        List<Comment> list = new LinkedList<>();
        // 判断下载资源名和登录用户名是否为空
        User sysUser = ShiroUtils.getSysUser();
        // 普通会员
        if (ShiroUtils.isOrdinary(sysUser)){
            list = commentService.selectJoint(comment.getArticleName(),sysUser.getLoginName(),
                    String.valueOf(comment.getParams().get("beginCommentDate")) ,String.valueOf(comment.getParams().get("endCommentDate")),
                    String.valueOf(comment.getCommentState()),null);
        }else {
            Long deptId = null;
            if (ShiroUtils.isCollegeAdmin(sysUser)){
                deptId = sysUser.getDeptId();
            }
            // 管理员
            list = commentService.selectJoint(comment.getArticleName(),comment.getLoginName(),
                    String.valueOf(comment.getParams().get("beginCommentDate")),String.valueOf(comment.getParams().get("endCommentDate")),
                    String.valueOf(comment.getCommentState()),deptId);
        }
        list.forEach(comment1 -> {
            comment1.setUser(userService.selectUserById(comment1.getCommentUserId()));
            comment1.setArticle(articleService.selectArticleById(comment1.getCommentArticleId()));
        });
        return getDataTable(list);
    }

    /**
     * 导出评论列表
     */
    @RequiresPermissions("content:comment:export")
    @Log(title = "评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Comment comment)
    {
        List<Comment> list = commentService.selectCommentList(comment);
        ExcelUtil<Comment> util = new ExcelUtil<Comment>(Comment.class);
        return util.exportExcel(list, "comment");
    }

    /**
     * 新增评论
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存评论
     */
    @RequiresPermissions("content:comment:add")
    @Log(title = "评论", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Comment comment)
    {
        return toAjax(commentService.insertComment(comment));
    }

    /**
     * 修改评论
     */
    @GetMapping("/edit/{commentId}")
    public String edit(@PathVariable("commentId") Long commentId, ModelMap mmap)
    {
        Comment comment = commentService.selectCommentById(commentId);
        mmap.put("comment", comment);
        return prefix + "/edit";
    }

    /**
     * 修改保存评论
     */
    @RequiresPermissions("content:comment:edit")
    @Log(title = "评论", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Comment comment)
    {
        return toAjax(commentService.updateComment(comment));
    }

    /**
     * 删除评论
     */
    @RequiresPermissions("content:comment:remove")
    @Log(title = "评论", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(commentService.deleteCommentByIds(ids));
    }
}
