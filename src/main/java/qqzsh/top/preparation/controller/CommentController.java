package qqzsh.top.preparation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.service.CommentService;

import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-27 15:18
 * @description 评论控制器
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 分页查询某个帖子的评论信息
     *
     * @param s_comment
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list")
    public List<Comment> list(Comment s_comment, @RequestParam(value = "page", required = false) Integer page) {
        s_comment.setState(1);
        return commentService.list(s_comment, page, 6, Sort.Direction.DESC, "commentDate");
    }

}

