package qqzsh.top.preparation.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-22 15:01
 * @description 首页或者跳转url控制器
 */
@Controller
public class IndexAdminController {

    /**
     * 跳转到管理员主页面
     * @return
     */
    @RequiresPermissions(value={"进入管理员主页"})
    @RequestMapping("/toAdminUserCenterPage")
    public ModelAndView toAdminUserCenterPage(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "管理员主页页面");
        mav.setViewName("admin/adminUserCenter");
        return mav;
    }

    @RequiresPermissions(value={"管理员默认页"})
    @RequestMapping("/admin/default")
    public ModelAndView toDefaultPage(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "管理员主页页面");
        mav.setViewName("admin/default");
        return mav;
    }


}

