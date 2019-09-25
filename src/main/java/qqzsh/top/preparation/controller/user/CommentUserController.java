package qqzsh.top.preparation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.CommentService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 22:01
 * @Description 用户-评论控制器
 */
@Controller
@RequestMapping("/user/comment")
public class CommentUserController {

    @Autowired
    private CommentService commentService;

    /**
     * 保存评论信息
     * @param comment
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/save")
    public Map<String,Object> save(Comment comment, HttpSession session)throws Exception{
        Map<String,Object> map=new HashMap<>();
        comment.setCommentDate(new Date());
        comment.setState(0);
        comment.setUser((User)session.getAttribute("currentUser"));
        commentService.save(comment);
        map.put("success", true);
        return map;
    }
}

