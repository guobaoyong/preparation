package qqzsh.top.preparation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.service.MessageService;
import qqzsh.top.preparation.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-21 21:43
 * @Description 用户页面跳转控制器
 */
@Controller
public class IndexUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    /**
     * 跳转用后中心页面
     *
     * @return
     */
    @RequestMapping("/toUserCenterPage")
    public ModelAndView toUserCenterPage(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        //获取消息数量
        Integer message = messageService.getCountByUserId(user.getId());
        //更新下session信息
        User byId = userService.findById(user.getId());
        byId.setMessageCount(message);
        session.setAttribute("currentUser", byId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "用户中心页面");
        mav.setViewName("user/userCenter");
        return mav;
    }

    @RequestMapping("/user/tobuyVipPage")
    public ModelAndView tobuyVipPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "购买VIP页面");
        mav.setViewName("user/buyVip");
        return mav;
    }
}

