package qqzsh.top.preparation.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.Message;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.entity.UserDownload;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.service.MessageService;
import qqzsh.top.preparation.service.UserDownloadService;
import qqzsh.top.preparation.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-02 22:04
 * @Description 管理员查看下载控制
 */
@Controller
@RequestMapping("/admin/message")
public class MessageAdminController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequiresPermissions(value={"分页查询消息信息"})
    @RequestMapping("/list")
    public Map<String,Object> list(Message message,
                                   @RequestParam(value="page",required=false)Integer page,
                                   @RequestParam(value="limit",required=false)Integer limit,
                                   @RequestParam(value="userName",required=false)String userName,
                                   @RequestParam(value="status",required=false)String status)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        if (userName != null && userName != ""){
            User user = userService.findByUserName(userName);
            if (user != null){
                message.setUser(user);
            }
        }
        if (status != null && status != ""){
            if (Integer.parseInt(status) == 0)
                message.setSee(false);
            else if (Integer.parseInt(status) == 1)
                message.setSee(true);
        }
        List<Message> userList = messageService.list(status,message,page, limit, Sort.Direction.DESC, "publishDate");
        List<Message> newList = new ArrayList<>();
        userList.forEach( message1 -> {
            message1.setUser(userService.getById(message1.getUser().getId()));
            newList.add(message1);
        });
        Long total = messageService.getTotal(status,message);
        resultMap.put("code", 0);
        resultMap.put("count", total);
        resultMap.put("data", newList);
        return resultMap;
    }

    /**
     * 根据id删除消息
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(value={"删除消息信息"})
    public Map<String,Object> delete(Integer id)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        //删除消息信息
        messageService.delete(id);
        resultMap.put("success", true);
        return resultMap;
    }


    /**
     * 多选删除
     * @param ids
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/deleteSelected")
    @RequiresPermissions(value={"删除消息信息"})
    public Map<String,Object> deleteSelected(String ids)throws Exception{
        String[] idsStr = ids.split(",");
        for(int i=0;i<idsStr.length;i++){
            // 删除消息信息
            messageService.delete(Integer.parseInt(idsStr[i]));
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions(value={"更新消息状态"})
    public Map<String,Object> updateStatus(Integer id, HttpSession session)throws Exception{
        Message byId = messageService.findById(id);
        byId.setSee(false);
        byId.setPublishDate(new Date());
        messageService.save(byId);
        //如果更新的是session用户
        User user=(User)session.getAttribute("currentUser");
        if (user.getId() == byId.getUser().getId()){
            user.setMessageCount(user.getMessageCount()+1);
            session.setAttribute("currentUser",user);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

}
