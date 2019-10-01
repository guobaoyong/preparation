package qqzsh.top.preparation.controller.user;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.entity.UserDownload;
import qqzsh.top.preparation.lucene.ArticleIndex;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.service.CommentService;
import qqzsh.top.preparation.service.UserDownloadService;
import qqzsh.top.preparation.service.UserService;
import qqzsh.top.preparation.util.DateUtil;
import qqzsh.top.preparation.util.RedisUtil;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-21 22:09
 * @Description 用户中心-资源帖子控制器
 */
@Controller
@RequestMapping("/user/article")
public class ArticleUserController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDownloadService userDownloadService;

    @Value("${articleImageFilePath}")
    private String articleImageFilePath;

    @Autowired
    private ArticleIndex articleIndex;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisUtil<Article> redisUtil;

    /**
     * Layui编辑器图片上传处理
     *
     * @param file 待上传文件
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadImage")
    public Map<String, Object> uploadImage(MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename(); // 获取文件名
            String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
            String newFileName = DateUtil.getCurrentDateStr() + suffixName; // 新文件名
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(articleImageFilePath + DateUtil.getCurrentDatePath() + newFileName));
            map.put("code", 0);
            map.put("msg", "上传成功");
            Map<String, Object> map2 = new HashMap<>();
            map2.put("src", "/image/" + DateUtil.getCurrentDatePath() + newFileName);
            map2.put("title", newFileName);
            map.put("data", map2);
        }
        return map;
    }

    /**
     * 根据条件分页查询资源帖子信息
     *
     * @param s_article
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/list")
    public Map<String, Object> list(Article s_article, HttpSession session, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        s_article.setUser(user);
        List<Article> articleList = articleService.list(s_article, page, limit, Sort.Direction.DESC, "publishDate");
        Long count = articleService.getTotal(s_article);
        resultMap.put("code", 0);
        resultMap.put("count", count);
        resultMap.put("data", articleList);
        return resultMap;
    }

    /**
     * 跳转到发布帖子页面
     *
     * @return
     */
    @RequestMapping("/toPublishArticlePage")
    public ModelAndView toPublishArticlePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "发布帖子页面");
        mav.setViewName("user/publishArticle");
        return mav;
    }

    /**
     * 跳转到帖子管理页面
     *
     * @return
     */
    @RequestMapping("/toArticleManagePage")
    public ModelAndView toArticleManagePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "帖子管理");
        mav.setViewName("user/articleManage");
        return mav;
    }

    /**
     * 跳转到帖子修改页面
     *
     * @return
     */
    @RequestMapping("/toModifyArticlePage/{id}")
    public ModelAndView toModifyArticlePage(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "帖子修改页面");
        mav.addObject("article", articleService.get(id));
        mav.setViewName("user/modifyArticle");
        return mav;
    }

    /**
     * 添加帖子
     *
     * @param article
     * @param sesion
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView add(Article article, HttpSession sesion) throws Exception {
        User user = (User) sesion.getAttribute("currentUser");
        article.setPublishDate(new Date());
        article.setUser(user);
        article.setState(1);
        article.setView(0);
        articleService.save(article);
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "发布帖子成功页面");
        mav.setViewName("user/publishArticleSuccess");
        return mav;
    }

    /**
     * 更新帖子
     *
     * @param article
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    public ModelAndView update(Article article) throws Exception {
        Article oldArticle = articleService.get(article.getId());
        oldArticle.setName(article.getName());
        oldArticle.setArcType(article.getArcType());
        oldArticle.setContent(article.getContent());
        oldArticle.setDownload1(article.getDownload1());
        oldArticle.setPassword1(article.getPassword1());
        oldArticle.setPoints(article.getPoints());
        // 假如审核未通过，用户点击修改的话 ，则重新审核
        if (oldArticle.getState() == 3) {
            oldArticle.setState(1);
        }
        articleService.save(oldArticle);
        if (oldArticle.getState() == 2) {
            //修改Lucene索引
            articleIndex.updateIndex(oldArticle);
            // redis缓存删除这个缓存
            redisUtil.del("article_"+oldArticle.getId());
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "修改帖子成功页面");
        mav.setViewName("user/modifyArticleSuccess");
        return mav;
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
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //删除该帖子的下载信息
        userDownloadService.deleteByArticleId(id);
        //删除该帖子下的所有评论
        commentService.deleteByArticleId(id);
        //删除帖子
        articleService.delete(id);
        //删除索引
        articleIndex.deleteIndex(String.valueOf(id));
        //删除redis索引
        redisUtil.del("article_"+id);
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
    public Map<String, Object> deleteSelected(String ids) throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            //删除该帖子下的下载信息
            userDownloadService.deleteByArticleId(Integer.parseInt(idsStr[i]));
            //删除该帖子下的所有评论
            commentService.deleteByArticleId(Integer.parseInt(idsStr[i]));
            //删除帖子
            articleService.delete(Integer.parseInt(idsStr[i]));
            //删除索引
            articleIndex.deleteIndex(idsStr[i]);
            //删除redis索引
            redisUtil.del("article_"+idsStr[i]);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 跳转到资源下载页面
     *
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/toDownLoadPage/{id}")
    public ModelAndView toDownLoadPage(@PathVariable("id") Integer id, HttpSession session) throws Exception {
        UserDownload userDownload = new UserDownload();
        Article article = articleService.get(id);

        User user = (User) session.getAttribute("currentUser");
        boolean isDownload = false; // 是否下载过
        Integer count = userDownloadService.getCountByUserIdAndArticleId(user.getId(), id);
        if (count > 0) {
            isDownload = true;
        } else {
            isDownload = false;
        }

        if (!isDownload) {
            // 用户积分是否够
            if (user.getPoints() - article.getPoints() < 0) {
                return null;
            }

            // 扣积分
            user.setPoints(user.getPoints() - article.getPoints());
            userService.save(user);

            // 给分享人加积分
            User articleUser = article.getUser();
            articleUser.setPoints(articleUser.getPoints() + article.getPoints());
            userService.save(articleUser);

            // 保存用户下载信息
            userDownload.setArticle(article);
            userDownload.setUser(user);
            userDownload.setDownloadDate(new Date());
            userDownloadService.save(userDownload);

        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("article", articleService.get(id));
        mav.setViewName("user/downloadPage");
        return mav;
    }

    /**
     * 跳转到VIP资源下载页面
     *
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/toVipDownLoadPage/{id}")
    public ModelAndView toVipDownLoadPage(@PathVariable("id") Integer id, HttpSession session) throws Exception {
        UserDownload userDownload = new UserDownload();
        Article article = articleService.get(id);

        User user = (User) session.getAttribute("currentUser");

        // 判断是否是vip
        if (!user.isVip()) {
            return null;
        }

        // 是否下载过
        boolean isDownload = false;
        Integer count = userDownloadService.getCountByUserIdAndArticleId(user.getId(), id);
        if (count > 0) {
            isDownload = true;
        } else {
            isDownload = false;
        }

        if (!isDownload) {
            // 保存用户下载信息
            userDownload.setArticle(article);
            userDownload.setUser(user);
            userDownload.setDownloadDate(new Date());
            userDownloadService.save(userDownload);

        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("article", articleService.get(id));
        mav.setViewName("user/downloadPage");
        return mav;
    }
}

