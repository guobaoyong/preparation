package qqzsh.top.preparation.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.entity.UserDownload;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.service.UserDownloadService;
import qqzsh.top.preparation.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-02 22:04
 * @Description 管理员查看下载控制
 */
@Controller
@RequestMapping("/admin/download")
public class DownloadAdminController {

    @Autowired
    private UserDownloadService userDownloadService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequiresPermissions(value = {"分页查询用户下载信息"})
    @RequestMapping("/list")
    public Map<String, Object> list(UserDownload userDownload,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "limit", required = false) Integer limit,
                                    @RequestParam(value = "userName", required = false) String userName,
                                    @RequestParam(value = "articleName", required = false) String articleName) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (articleName != null && articleName != "") {
            List<Article> name = articleService.findByNameLike(articleName);
            List<UserDownload> finalList = new ArrayList<>();
            Long finalTotal = 0L;
            if (name.size() != 0) {
                for (int i = 0; i < name.size(); i++) {
                    userDownload.setArticle(name.get(i));
                    if (userName != null && userName != "") {
                        User user = userService.findByUserName(userName);
                        if (user != null) {
                            userDownload.setUser(user);
                        } else {
                            resultMap.put("code", 0);
                            resultMap.put("count", finalTotal);
                            resultMap.put("data", finalList);
                            return resultMap;
                        }
                    }

                    List<UserDownload> userList = userDownloadService.list(userDownload, page, limit, Sort.Direction.DESC, "downloadDate");
                    List<UserDownload> newList = new ArrayList<>();
                    userList.forEach(userDownload1 -> {
                        userDownload1.setUser(userService.getById(userDownload1.getUser().getId()));
                        userDownload1.setArticle(articleService.get(userDownload1.getArticle().getId()));
                        newList.add(userDownload1);
                    });
                    Long total = userDownloadService.getTotal(userDownload);
                    finalList.addAll(newList);
                    finalTotal += total;
                }
            }
            resultMap.put("code", 0);
            resultMap.put("count", finalTotal);
            resultMap.put("data", finalList);
            return resultMap;
        }
        if (userName != null && userName != "") {
            User user = userService.findByUserName(userName);
            if (user != null) {
                userDownload.setUser(user);
            } else {
                resultMap.put("code", 0);
                resultMap.put("count", 0L);
                resultMap.put("data", new ArrayList<>());
                return resultMap;
            }
        }
        List<UserDownload> userList = userDownloadService.list(userDownload, page, limit, Sort.Direction.DESC, "downloadDate");
        List<UserDownload> newList = new ArrayList<>();
        userList.forEach(userDownload1 -> {
            userDownload1.setUser(userService.getById(userDownload1.getUser().getId()));
            userDownload1.setArticle(articleService.get(userDownload1.getArticle().getId()));
            newList.add(userDownload1);
        });
        Long total = userDownloadService.getTotal(userDownload);
        resultMap.put("code", 0);
        resultMap.put("count", total);
        resultMap.put("data", newList);
        return resultMap;
    }

    /**
     * 根据id删除帖子
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"删除下载信息"})
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //删除用户下载帖子信息
        userDownloadService.delete(id);
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
    @RequiresPermissions(value = {"删除下载信息"})
    public Map<String, Object> deleteSelected(String ids) throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            // 删除用户下载帖子信息
            userDownloadService.delete(Integer.parseInt(idsStr[i]));
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

}
