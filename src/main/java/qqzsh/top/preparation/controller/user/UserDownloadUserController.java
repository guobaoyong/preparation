package qqzsh.top.preparation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.entity.UserDownload;
import qqzsh.top.preparation.service.UserDownloadService;
import qqzsh.top.preparation.util.PageUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:12
 * @Description 用户-用户下载控制器
 */
@Controller
@RequestMapping("/user/userDownload")
public class UserDownloadUserController {

    @Autowired
    private UserDownloadService userDownloadService;

    /**
     * 判断资源是否下载过
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/exist")
    public boolean exist(Integer id,HttpSession session)throws Exception{
        User user = (User) session.getAttribute("currentUser");
        Integer count = userDownloadService.getCountByUserIdAndArticleId(user.getId(), id);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断用户积分是否足够下载这个资源
     * @param points
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/enough")
    public boolean enough(Integer points, HttpSession session)throws Exception{
        User user = (User) session.getAttribute("currentUser");
        if(user.getPoints()>=points){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 分页查询用户下载资源信息
     * @param session
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/list/{id}")
    public ModelAndView list(HttpSession session, @PathVariable(value="id",required=false)Integer page)throws Exception{
        ModelAndView mav=new ModelAndView();
        User user=(User)session.getAttribute("currentUser");
        UserDownload s_userDownload=new UserDownload();
        s_userDownload.setUser(user);
        List<UserDownload> userDownloadList = userDownloadService.list(s_userDownload, page, 10, Sort.Direction.DESC, "downloadDate");
        Long total = userDownloadService.getTotal(s_userDownload);
        mav.addObject("userDownloadList", userDownloadList);
        mav.addObject("pageCode", PageUtil.genPagination("/user/userDownload/list", total, page, 10, ""));
        mav.addObject("title", "用户已下载资源页面");
        mav.setViewName("user/listUserDownload");
        return mav;
    }

}

