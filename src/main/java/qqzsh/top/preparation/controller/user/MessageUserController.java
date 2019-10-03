package qqzsh.top.preparation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Message;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.MessageService;
import qqzsh.top.preparation.util.PageUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 13:36
 * @Description 用户中心-消息控制器
 */
@Controller
@RequestMapping("/user/message")
public class MessageUserController {

    @Autowired
    private MessageService messageService;

    /**
     * 查看系统消息 状态都改成已经查看
     *
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/see")
    public ModelAndView see(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("currentUser");
        Message s_message = new Message();
        s_message.setUser(user);
        messageService.updateState(user.getId());
        //设置未读条数为0
        user.setMessageCount(0);
        //更新session用户
        session.setAttribute("currentUser", user);

        List<Message> messageList = messageService.list(null, s_message, 1, 10, Sort.Direction.DESC, "publishDate");
        Long total = messageService.getTotal(null, s_message);
        ModelAndView mav = new ModelAndView();
        mav.addObject("messageList", messageList);
        mav.addObject("pageCode", PageUtil.genPagination("/user/message/list", total, 1, 10, ""));
        mav.addObject("title", "系统消息页面");
        mav.setViewName("user/listMessage");
        return mav;
    }

    /**
     * 分页查询消息帖子
     *
     * @param session
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/list/{id}")
    public ModelAndView list(HttpSession session, @PathVariable(value = "id", required = false) Integer page) throws Exception {
        ModelAndView mav = new ModelAndView();
        User user = (User) session.getAttribute("currentUser");
        Message s_message = new Message();
        s_message.setUser(user);
        List<Message> messageList = messageService.list(null, s_message, page, 10, Sort.Direction.DESC, "publishDate");
        Long total = messageService.getTotal(null, s_message);
        mav.addObject("messageList", messageList);
        mav.addObject("pageCode", PageUtil.genPagination("/user/message/list", total, page, 10, ""));
        mav.addObject("title", "系统消息页面");
        mav.setViewName("user/listMessage");
        return mav;
    }
}

