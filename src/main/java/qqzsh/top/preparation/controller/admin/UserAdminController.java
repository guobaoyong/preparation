package qqzsh.top.preparation.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.Message;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.lucene.ArticleIndex;
import qqzsh.top.preparation.service.*;
import qqzsh.top.preparation.util.CryptographyUtil;
import qqzsh.top.preparation.util.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-24 20:26
 * @Description 管理员-用户控制器
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController implements ServletContextListener {

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil<Integer> redisUtil;

    private static ServletContext application;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserDownloadService userDownloadService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleIndex articleIndex;


    /**
     * 根据条件分页查询用户信息
     *
     * @param s_user
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"分页查询用户信息"})
    @RequestMapping("/list")
    public Map<String, Object> list(User s_user, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<User> userList = userService.list(s_user, page, limit, Sort.Direction.DESC, "registerDate");
        Long total = userService.getTotal(s_user);
        resultMap.put("code", 0);
        resultMap.put("count", total);
        resultMap.put("data", userList);
        return resultMap;
    }

    /**
     * 重置用户密码
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"重置用户密码"})
    @RequestMapping("/resetPassword")
    public Map<String, Object> resetPassword(Integer id) throws Exception {
        User oldUser = userService.getById(id);
        oldUser.setPassword(CryptographyUtil.md5("123456", CryptographyUtil.SALT));
        userService.save(oldUser);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 用户积分充值
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"用户积分充值"})
    @RequestMapping("/addPoints")
    public Map<String, Object> addPoints(User user, HttpSession session) throws Exception {
        //session用户
        User currentUser = (User) session.getAttribute("currentUser");
        //要更新用户
        User oldUser = userService.getById(user.getId());
        oldUser.setPoints(oldUser.getPoints() + user.getPoints());
        userService.save(oldUser);
        //如何要更新用户就是session用户
        if (currentUser.getId() == oldUser.getId()) {
            currentUser.setPoints(oldUser.getPoints() + user.getPoints());
            //更新下session用户
            session.setAttribute("currentUser", currentUser);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 修改用户VIP状态
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"修改用户VIP状态"})
    @RequestMapping("/updateVipState")
    public Map<String, Object> updateVipState(User user, HttpSession session) throws Exception {
        //session用户
        User currentUser = (User) session.getAttribute("currentUser");
        //要更新用户
        User oldUser = userService.getById(user.getId());
        oldUser.setVip(user.isVip());
        //设置VIP时间
        Calendar rightNow = Calendar.getInstance();
        Date date = user.getEndtime() == null ? new Date() : user.getEndtime();
        rightNow.setTime(date);
        if (user.isVip()){
            //在当前时间上加入50年
            rightNow.add(Calendar.YEAR, 50);
            oldUser.setEndtime(rightNow.getTime());
        }else {
            oldUser.setEndtime(rightNow.getTime());
        }
        userService.save(oldUser);
        //如何要更新用户就是session用户
        if (currentUser.getId() == oldUser.getId()) {
            currentUser.setVip(user.isVip());
            //更新下session用户
            session.setAttribute("currentUser", currentUser);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 修改用户状态
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"修改用户状态"})
    @RequestMapping("/updateUserState")
    public Map<String, Object> updateUserState(User user) throws Exception {
        User oldUser = userService.getById(user.getId());
        oldUser.setOff(user.isOff());
        userService.save(oldUser);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        application = sce.getServletContext();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @ResponseBody
    @RequiresPermissions(value = {"清除签到信息"})
    @RequestMapping("/clearSign")
    public Map<String, Object> clearSign() throws Exception {
        application.setAttribute("signTotal", 0);
        redisUtil.set("signTotal", 0);
        userService.updateAllSignInfo();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 修改用户角色
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"修改用户角色"})
    @RequestMapping("//updateRole")
    public Map<String, Object> updateRole(User user, HttpSession session) throws Exception {
        //session用户
        User currentUser = (User) session.getAttribute("currentUser");
        //要更新用户
        User oldUser = userService.getById(user.getId());
        if (user.getRoleName().equals("true")) {
            oldUser.setRoleName("管理员");
        } else {
            oldUser.setRoleName("会员");
        }
        userService.save(oldUser);
        //如何要更新用户就是session用户
        if (currentUser.getId() == oldUser.getId()) {
            currentUser.setRoleName(user.getRoleName());
            //更新下session用户
            session.setAttribute("currentUser", currentUser);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequiresPermissions(value = {"发送消息"})
    @PostMapping("/sentMessage")
    public Map<String, Object> sentMessage(Integer userId, String content, HttpSession session) throws Exception {
        //session用户
        User currentUser = (User) session.getAttribute("currentUser");
        //封装消息实体
        User user = new User();
        user.setId(userId);
        Message message = new Message();
        message.setUser(user);
        message.setContent(content);
        message.setSee(false);
        message.setPublishDate(new Date());
        messageService.save(message);

        //如何要更新用户就是session用户
        if (currentUser.getId() == userId) {
            currentUser.setMessageCount(currentUser.getMessageCount() + 1);
            //更新下session用户
            session.setAttribute("currentUser", currentUser);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"删除用户信息"})
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //删除该用户所有订单
        orderService.deleteByUserId(id);
        //删除该用户所有消息
        messageService.deleteByUserId(id);
        //删除该用户所有已下载信息
        userDownloadService.deleteByUserId(id);
        //删除所有评论
        commentService.deleteByUserId(id);
        //找到所有帖子
        List<Article> byUserId = articleService.findByUserId(id);
        for (int i = 0; i < byUserId.size(); i++) {
            //删除用户下载帖子信息
            userDownloadService.deleteByArticleId(byUserId.get(i).getId());
            //删除该帖子下的所有评论
            commentService.deleteByArticleId(byUserId.get(i).getId());
            //删除帖子
            articleService.delete(byUserId.get(i).getId());
            //删除索引
            articleIndex.deleteIndex(String.valueOf(byUserId.get(i).getId()));
            //删除redis索引
            redisUtil.del("article_" + byUserId.get(i).getId());
        }
        //删除用户信息
        userService.delete(id);
        //清空redis关于数量的变量
        redisUtil.del("userNum");
        redisUtil.del("articleNums");
        redisUtil.del("downloadNums");
        redisUtil.del("orderNums");
        resultMap.put("success", true);
        return resultMap;
    }


    /**
     * 多选删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/deleteSelected")
    @RequiresPermissions(value = {"删除用户信息"})
    public Map<String, Object> deleteSelected(String ids) throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            int id = Integer.parseInt(idsStr[i]);
            //删除该用户所有订单
            orderService.deleteByUserId(id);
            //删除该用户所有消息
            messageService.deleteByUserId(id);
            //删除该用户所有已下载信息
            userDownloadService.deleteByUserId(id);
            //删除所有评论
            commentService.deleteByUserId(id);
            //找到所有帖子
            List<Article> byUserId = articleService.findByUserId(id);
            for (int j = 0; j < byUserId.size(); j++) {
                //删除用户下载帖子信息
                userDownloadService.deleteByArticleId(byUserId.get(j).getId());
                //删除该帖子下的所有评论
                commentService.deleteByArticleId(byUserId.get(j).getId());
                //删除帖子
                articleService.delete(byUserId.get(j).getId());
                //删除索引
                articleIndex.deleteIndex(String.valueOf(byUserId.get(j).getId()));
                //删除redis索引
                redisUtil.del("article_" + byUserId.get(j).getId());
            }
            //删除用户信息
            userService.delete(id);
        }
        //清空redis关于数量的变量
        redisUtil.del("userNum");
        redisUtil.del("articleNums");
        redisUtil.del("downloadNums");
        redisUtil.del("orderNums");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }
}

