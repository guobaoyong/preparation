package qqzsh.top.preparation.project.front.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;
import qqzsh.top.preparation.common.constant.UserConstants;
import qqzsh.top.preparation.common.utils.*;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.config.PreparationConfig;
import qqzsh.top.preparation.framework.shiro.service.PasswordService;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.change.domain.PointChange;
import qqzsh.top.preparation.project.content.change.service.IPointChangeService;
import qqzsh.top.preparation.project.content.comment.domain.Comment;
import qqzsh.top.preparation.project.content.comment.service.ICommentService;
import qqzsh.top.preparation.project.content.download.domain.UserDownload;
import qqzsh.top.preparation.project.content.download.service.IUserDownloadService;
import qqzsh.top.preparation.project.content.link.domain.Link;
import qqzsh.top.preparation.project.content.link.service.ILinkService;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.project.front.lucene.ArticleIndex;
import qqzsh.top.preparation.project.system.dept.domain.Dept;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.notice.domain.Notice;
import qqzsh.top.preparation.project.system.notice.service.INoticeService;
import qqzsh.top.preparation.project.system.post.service.IPostService;
import qqzsh.top.preparation.project.system.user.domain.User;
import qqzsh.top.preparation.project.system.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-30 20:53
 * @description
 */
@Controller
@Slf4j
public class FrontIndexController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private IArcTypeService arcTypeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private JavaMailSender mailSender;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private ILinkService linkService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserDownloadService userDownloadService;

    @Autowired
    private ArticleIndex articleIndex;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IPointChangeService pointChangeService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IPostService postService;

    /**
     * 默认lucene的地址
     */
    private static String lucenePath = PreparationConfig.getProfile() + "/lucene/";

    /**
     * 打开首页V1方法
     * @param request
     * @return
     */
    @GetMapping("/V1")
    public ModelAndView index(HttpServletRequest request){
        request.getSession().setAttribute("tMenu", "t_0");
        ModelAndView data = getData(null,null,"");
        data.addObject("title", "首页");
        if (redisUtil.hasKey("signTotal")){
            data.addObject("signTotal", redisUtil.get("signTotal"));
        }else {
            redisUtil.set("signTotal", 0);
            data.addObject("signTotal", 0);
        }
        data.setViewName("front/index");
        return data;
    }

    /**
     * 首页、分页、详情获取数据
     * @return
     */
    public ModelAndView getData(Long typeId, Integer page,String param){
        ModelAndView modelAndView = new ModelAndView();
        // 获取最新一条通知
        Notice newOne = null;
        if (redisUtil.hasKey("notice")){
            newOne = (Notice) redisUtil.get("notice");
        }else {
            newOne = noticeService.getNewOne();
            redisUtil.set("notice",newOne);
        }
        modelAndView.addObject("newOne", newOne);

        // 获取最新一条广告
        Notice newOneAD = null;
        if (redisUtil.hasKey("ad")){
            newOneAD = (Notice) redisUtil.get("ad");
        }else {
            newOneAD = noticeService.getNewOneAD();
            redisUtil.set("ad",newOneAD);
        }
        modelAndView.addObject("newOneAD", newOneAD);

        Article article = new Article();
        // 审核通过的帖子
        article.setArticleState(1L);
        if (page == null){
            page = 1;
        }
        if (typeId != null) {
            ArcType arcType = arcTypeService.selectArcTypeById(typeId);
            article.setArticleTypeId(typeId);
            modelAndView.addObject("title", arcType.getSrcTypeName() + "-第" + page + "页");
        }else {
            modelAndView.addObject("title", "第" + page + "页");
        }
        startPage(page,20,"article_publish_date desc");
        List<Article> list = articleService.selectArticleList(article);
        List<Article> newList = new LinkedList<>();
        list.forEach(article1 -> {
            article1.setUser(userService.selectUserById(article1.getArticleUserId()));
            newList.add(article1);
        });
        modelAndView.addObject("articleList", newList);

        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            //每个资源类别的数量
            List<ArcType> arc_type_list = (List<ArcType>) redisUtil.get("arc_type_list");
            modelAndView.addObject("allArcTypeList",arc_type_list);
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            arcTypes.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                arcType.setAllSize(articleService.selectArticleList(article1).size());
            });
            redisUtil.set("arc_type_list",arcTypes);
            modelAndView.addObject("allArcTypeList",arcTypes);
        }


        // 获取总共多少页
        modelAndView.addObject("pageCode", PageUtil.genPagination("/article/list", new PageInfo(list).getTotal(), page, 20, param));

        // 热门资源
        startPage(1,43,"article_publish_date desc");
        article.setArticleIsHot(true);
        List<Article> indexHotArticleList = articleService.selectArticleList(article);
        modelAndView.addObject("hotArticleList", indexHotArticleList);

        //友情链接
        if (redisUtil.hasKey("link_list")){
            modelAndView.addObject("allLinkList",redisUtil.get("link_list"));
        }else {
            // 从数据库中查询所有友情链接并放入redis
            List<Link> links = linkService.selectLinkList(null);
            redisUtil.set("link_list",links);
            modelAndView.addObject("allLinkList",links);
        }

        //用户数、资源总数、下载量
        //redis获取数据-用户数
        if (!redisUtil.hasKey("userNum")) {
            redisUtil.set("userNum", userService.selectUserList(new User()).size());
        }
        modelAndView.addObject("userNum", redisUtil.get("userNum"));

        //redis获取数据-资源数
        if (!redisUtil.hasKey("articleNums")) {
            redisUtil.set("articleNums", articleService.selectArticleList(new Article()).size());
        }
        modelAndView.addObject("articleNums", redisUtil.get("articleNums"));

        //redis获取数据-总下载数
        if (!redisUtil.hasKey("downloadNums")) {
            redisUtil.set("downloadNums", userDownloadService.selectUserDownloadList(new UserDownload()).size());
        }
        modelAndView.addObject("downloadNums", redisUtil.get("downloadNums"));


        return modelAndView;
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(Integer pageNum,Integer pageSize,String orderBy) {
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 根据条件分页查询资源帖子信息
     *
     * @return
     */
    @RequestMapping("/article/list/{id}")
    public ModelAndView list(@RequestParam(value = "typeId", required = false) Long typeId,
                             @PathVariable(value = "id", required = false) Integer page,
                             HttpServletRequest request) {
        StringBuffer param = new StringBuffer();
        if (typeId != null) {
            param.append("?typeId=" + typeId);
        }
        if (typeId != null) {
            request.getSession().setAttribute("tMenu", "t_" + typeId);
        }
        ModelAndView data = getData(typeId, page,param.toString());
        data.setViewName("front/index");
        return data;
    }

    /**
     * 打开注册页面
     *
     * @return
     */
    @GetMapping("/toRegister")
    public ModelAndView toRegister() {
        ModelAndView modelAndView = new ModelAndView("front/register");
        // 放入高校
        modelAndView.addObject("depts",depts());
        // 放入职称
        modelAndView.addObject("posts",postService.selectPostAll());
        return modelAndView;
    }

    /**
     * 获取顶层所有高校
     */
    public List<Dept> depts(){
        Dept dept = new Dept();
        // 顶层高校
        dept.setParentId(100L);
        List<Dept> depts = deptService.selectDeptList(dept);
        return depts;
    }

    /**
     * 打开登陆页面
     *
     * @return
     */
    @GetMapping("/toLogin")
    public ModelAndView toLogin() {
        return new ModelAndView("front/login");
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/register")
    public Map<String, Object> register(@Valid User user,
                                        Long postId,
                                        BindingResult bindingResult,
                                        String vaptcha_token,
                                        HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            map.put("success", false);
            map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
        } else if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
            map.put("success", false);
            map.put("errorInfo", "用户名已存在，请更换！");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            map.put("success", false);
            map.put("errorInfo", "邮箱已存在，请更换！");
        } else if (!vaptchaCheck(vaptcha_token, request.getRemoteHost())) {
            map.put("success", false);
            map.put("errorInfo", "人机验证失败！");
        } else {
            // 普通用户
            user.setRoleIds(new Long[]{2L});
            user.setVipTime(new Date());
            if (StringUtils.isBlank(user.getUserName())){
                user.setUserName("注册用户_"+user.getVipTime().getTime());
            }
            user.setPostIds(new Long[]{postId});
            userService.insertUser(user);
            map.put("success", true);
        }
        return map;
    }

    /**
     * 人机验证结果判断
     *
     * @param token
     * @param ip
     * @return
     * @throws Exception
     */
    private boolean vaptchaCheck(String token, String ip) throws Exception {
        String body = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://api.vaptcha.com/v2/validate");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("id", "5d8474d0fc650e5af4ec42f7"));
        nvps.add(new BasicNameValuePair("secretkey", "b55e0e1e018f4d148221d296dcffce89"));
        nvps.add(new BasicNameValuePair("scene", ""));
        nvps.add(new BasicNameValuePair("token", token));
        nvps.add(new BasicNameValuePair("ip", ip));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        CloseableHttpResponse r = httpClient.execute(httpPost);
        HttpEntity entity = r.getEntity();

        if (entity != null) {
            body = EntityUtils.toString(entity, "utf-8");
            System.out.println(body);
        }
        r.close();
        httpClient.close();
        Gson gson = new Gson();
        VaptchaMessage message = gson.fromJson(body, VaptchaMessage.class);
        if (message.getSuccess() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开找回密码页面
     *
     * @return
     */
    @GetMapping("/findPassword")
    public ModelAndView findPassword() {
        return new ModelAndView("front/findPassword");
    }

    /**
     * 打开修改密码页面
     *
     * @return
     */
    @GetMapping("/modifyPassword")
    public ModelAndView modifyPassword() {
        return new ModelAndView("front/modifyPassword");
    }

    /**
     * 发送邮件
     *
     * @param email
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/sendEmail")
    public Map<String, Object> sendEmail(String email, HttpSession session) throws Exception {
        Map<String, Object> resulMap = new HashMap<>();
        if (StringUtil.isEmpty(email)) {
            resulMap.put("success", false);
            resulMap.put("errorInfo", "邮件不能为空！");
            return resulMap;
        }
        User u = userService.selectUserByEmail(email);
        if (u == null) {
            resulMap.put("success", false);
            resulMap.put("errorInfo", "这个邮件不存在！");
            return resulMap;
        }
        //生成六位随机数
        String mailCode = StringUtils.genSixRandomNum();
        log.info("mailCode:" + mailCode);
        SimpleMailMessage message = new SimpleMailMessage();
        // 发件人
        message.setFrom("43240825@qq.com");
        message.setTo(email);
        // 主题
        message.setSubject(configService.getKey("site.name")+"-用户找回密码");
        message.setText("验证码：" + mailCode + "\n此验证码只可以验证一次");
        mailSender.send(message);

        // 验证码存到session中
        session.setAttribute("mailCode", mailCode);
        session.setAttribute("userId", u.getUserId());
        resulMap.put("success", true);
        return resulMap;
    }

    /**
     * 邮件验证码判断 重置
     *
     * @param yzm
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/checkYzm")
    public Map<String, Object> checkYzm(String yzm, HttpSession session) throws Exception {
        Map<String, Object> resulMap = new HashMap<>();
        if (StringUtil.isEmpty(yzm)) {
            resulMap.put("success", false);
            resulMap.put("errorInfo", "验证码不能为空！");
            return resulMap;
        }
        String mailCode = (String) session.getAttribute("mailCode");
        Long userId = (Long) session.getAttribute("userId");
        if (!yzm.equals(mailCode)) {
            resulMap.put("success", false);
            resulMap.put("errorInfo", "验证码错误！");
            return resulMap;
        }
        User user = userService.selectUserById(userId);
        user.setPassword("123456");
        userService.updateUserInfo(user);
        //将session中的验证码清空
        session.removeAttribute("mailCode");
        session.removeAttribute("userId");
        resulMap.put("success", true);
        return resulMap;
    }

    /**
     * 修改密码
     *
     * @param oldpassword
     * @param password
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/modifyPassword")
    public Map<String, Object> modifyPassword(String oldpassword,
                                              String password, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("currentUser");
        user = userService.selectUserById(user.getUserId());
        Map<String, Object> resulMap = new HashMap<>();
        if (!user.getPassword().equals(passwordService.encryptPassword(user.getLoginName(), oldpassword, user.getSalt()))) {
            resulMap.put("success", false);
            resulMap.put("errorInfo", "原密码错误！");
            return resulMap;
        }
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), password, user.getSalt()));
        userService.updateUserInfo(user);
        resulMap.put("success", true);
        return resulMap;
    }

    /**
     * 根据id查询帖子详细信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/article/detail/{id}")
    public ModelAndView view(@PathVariable("id") Long id) throws Exception {
        ModelAndView data = getData(null, null, "");
        Article article = null;
        String key = "article_" + id;
        if (redisUtil.hasKey(key)) {
            article = (Article) redisUtil.get(key);
        } else {
            article = articleService.selectArticleById(id);
            redisUtil.set(key, article, 5 * 60);
        }
        article.setUser(userService.selectUserById(article.getArticleUserId()));
        //查看次数由数据库获取
        article.setArticleView(articleService.selectArticleById(id).getArticleView());
        // 下载次数
        article.setTotal(articleService.selectDownloadCount(article.getArticleId()));
        // 来源
        article.setRemark(deptService.selectDeptById(article.getUser().getDeptId()).getDeptName());
        data.addObject("article", article);
        data.addObject("title", article.getArticleName());
        Comment s_comment = new Comment();
        s_comment.setCommentArticleId(article.getArticleId());
        // 审核通过的评论信息
        s_comment.setCommentState(1L);
        // 总共多少条评论
        data.addObject("commentCount",commentService.selectCommentList(s_comment).size());
        data.setViewName("front/article");
        return data;
    }

    /**
     * 查看次数加1
     *
     * @param articleId
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/updateView")
    public void updateView(Long articleId) throws Exception {
        Article article = articleService.selectArticleById(articleId);
        try {
            article.setArticleView(article.getArticleView() + 1);
        }catch (Exception e){
            article.setArticleView(1L);
        }
        articleService.updateArticle(article);
    }

    /**
     * 分页查询某个帖子的评论信息
     *
     * @param comment
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment/list")
    public List<Comment> list(Comment comment, @RequestParam(value = "page", required = false,defaultValue = "1") Integer page) {
        comment.setCommentState(1L);
        startPage(page,6,"comment_date desc");
        List<Comment> comments = commentService.selectCommentList(comment);
        List<Comment> newList = new LinkedList<>();
        comments.forEach(comment1 -> {
            comment1.setUser(userService.selectUserById(comment1.getCommentUserId()));
            newList.add(comment1);
        });
        return newList;
    }

    /**
     * 判断资源是否下载过
     *
     * @param articleId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/userDownload/exist")
    public boolean exist(Long articleId) {
        User user = ShiroUtils.getSysUser();
        UserDownload userDownload = new UserDownload();
        userDownload.setDownloadArticleId(articleId);
        userDownload.setDownloadUserId(user.getUserId());
        Integer count = userDownloadService.selectUserDownloadList(userDownload).size();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断用户积分是否足够下载这个资源
     *
     * @param points
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/userDownload/enough")
    public boolean enough(Integer points) {
        User user = ShiroUtils.getSysUser();
        if (user.getPoint() >= points) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳转到资源下载页面
     *
     * @param articleId
     * @return
     * @throws Exception
     */
    @RequestMapping("/article/toDownLoadPage/{id}")
    public ModelAndView toDownLoadPage(@PathVariable("id") Long articleId) {

        UserDownload userDownload = new UserDownload();
        Article article = articleService.selectArticleById(articleId);
        User user = ShiroUtils.getSysUser();
        userDownload.setDownloadArticleId(articleId);
        userDownload.setDownloadUserId(user.getUserId());

        // 是否下载过
        boolean isDownload = false;
        Integer count = userDownloadService.selectUserDownloadList(userDownload).size();
        if (count > 0) {
            isDownload = true;
        } else {
            isDownload = false;
        }

        if (!isDownload) {
            // 用户积分是否够
            if (user.getPoint() - article.getArticlePoints() < 0) {
                return null;
            }

            //积分变更记录
            PointChange pointChange = new PointChange();
            pointChange.setPointContent("下载资源【"+ article.getArticleName()+"】");
            pointChange.setPointFront(Long.parseLong(String.valueOf(user.getPoint())));
            pointChange.setPointEnd(Long.parseLong(String.valueOf(user.getPoint() - article.getArticlePoints())));
            pointChange.setPointChange(Long.parseLong(String.valueOf(article.getArticlePoints())));
            pointChange.setPointUserId(user.getUserId());
            pointChange.setPointStatus(2);
            pointChange.setPointLoginName(user.getLoginName());
            pointChange.setPointCreateTime(DateUtils.getNowDate());
            pointChange.setPointUpdateTime(DateUtils.getNowDate());
            pointChange.setPointSymbol("-");
            pointChangeService.insertPointChange(pointChange);
            // 扣积分
            user.setPoint(user.getPoint() - article.getArticlePoints());
            userService.updateUserInfo(user);

            //积分变更记录2
            User articleUser = userService.selectUserById(article.getArticleUserId());
            PointChange pointChange2 = new PointChange();
            pointChange2.setPointContent("资源【"+ article.getArticleName()+"】被【"+user.getLoginName()+"】下载");
            pointChange2.setPointFront(Long.parseLong(String.valueOf(articleUser.getPoint())));
            pointChange2.setPointEnd(Long.parseLong(String.valueOf(articleUser.getPoint() + article.getArticlePoints())));
            pointChange2.setPointChange(Long.parseLong(String.valueOf(article.getArticlePoints())));
            pointChange2.setPointUserId(articleUser.getUserId());
            pointChange2.setPointStatus(2);
            pointChange2.setPointLoginName(articleUser.getLoginName());
            pointChange2.setPointCreateTime(DateUtils.getNowDate());
            pointChange2.setPointUpdateTime(DateUtils.getNowDate());
            pointChange2.setPointSymbol("+");
            pointChangeService.insertPointChange(pointChange2);

            // 给分享人加积分
            articleUser.setPoint(articleUser.getPoint() + article.getArticlePoints());
            userService.updateUserInfo(articleUser);

            // 保存用户下载信息
            userDownload.setDownloadArticleId(article.getArticleId());
            userDownload.setDownloadUserId(user.getUserId());
            userDownload.setDownloadDate(new Date());
            userDownloadService.insertUserDownload(userDownload);

            //更新redis
            if (!redisUtil.hasKey("downloadNums")) {
                redisUtil.set("downloadNums", userDownloadService.selectUserDownloadList(null).size());
            } else {
                int num = (int) redisUtil.get("downloadNums");
                redisUtil.delete("downloadNums");
                redisUtil.set("downloadNums", num + 1);
            }

        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("article", articleService.selectArticleById(articleId));
        mav.setViewName("front/downloadPage");
        return mav;
    }

    /**
     * 保存评论信息
     *
     * @param comment
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/comment/save")
    public Map<String, Object> save(Comment comment) {
        Map<String, Object> map = new HashMap<>();
        comment.setCommentDate(new Date());
        comment.setCommentState(0L);
        comment.setCommentUserId(ShiroUtils.getUserId());
        commentService.insertComment(comment);
        map.put("success", true);
        return map;
    }

    /**
     * 加载相关资源
     *
     * @param q
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/article/loadRelatedResources")
    public List<Article> loadRelatedResources(String q) throws Exception {
        if (StringUtil.isEmpty(q)) {
            return null;
        }
        List<Article> articleList = articleIndex.searchNoHighLighter(q);
        return articleList;
    }

    /**
     * 生成所有帖子索引
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/genAllIndex")
    @RequiresPermissions("index:restart:gen")
    public ModelAndView genAllIndex() {
        ModelAndView modelAndView = new ModelAndView();
        log.info("lucenePath:"+lucenePath);
        // 1.删除原来的索引
        try {
            File file = new File(lucenePath);
            // 通过JDK的File类的delete方法进行删除
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            // 写入索引信息失败
            modelAndView.addObject("status",false);
        }
        // 2.添加新的索引
        Article article1 = new Article();
        article1.setArticleState(1L);
        List<Article> articleList = articleService.selectArticleList(article1);
        int i = 1;
        for (Article article : articleList) {
            log.info("索引写入进度："+((double) (i++) / articleList.size()*100)+"%");
            // 高校
            article.setDept(deptService.selectDeptById(article.getArticleDeptId()));
            // 资源类别
            article.setArcType(arcTypeService.selectArcTypeById(article.getArticleTypeId()));
            if (!articleIndex.addIndex(article)) {
                modelAndView.addObject("status",false);
            }
        }
        // 写入索引信息成功
        modelAndView.addObject("status",true);
        // 返回前台页面
        modelAndView.setViewName("common/genIndex");
        return modelAndView;
    }

    /**
     * 用户签到
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/user/sign")
    public Map<String, Object> sign(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (session.getAttribute("currentUser") == null) {
            map.put("success", false);
            map.put("errorInfo", "请先登录，才能签到;");
            return map;
        }
        User user = ShiroUtils.getSysUser();
        if (userService.selectUserById(user.getUserId()).isSign()) {
            map.put("success", false);
            map.put("errorInfo", "尊敬的会员，你已经签到了，不能重复签到;");
            return map;
        }
        Integer signTotal = 0;
        if (redisUtil.hasKey("signTotal")){
            signTotal = (Integer) redisUtil.get("signTotal");
        }else {
            redisUtil.set("signTotal", 0);
        }
        redisUtil.set("signTotal", signTotal + 1);

        // 更新到数据库
        User newUser = userService.selectUserById(user.getUserId());

        //加入积分变更记录
        PointChange pointChange = new PointChange();
        pointChange.setPointContent("用户签到，日期:"+ DateUtils.formatDate(DateUtils.getNowDate()));
        pointChange.setPointFront(Long.parseLong(String.valueOf(newUser.getPoint())));
        pointChange.setPointEnd(Long.parseLong(String.valueOf(newUser.getPoint())) + 3);
        pointChange.setPointChange(3L);
        pointChange.setPointUserId(newUser.getUserId());
        pointChange.setPointStatus(2);
        pointChange.setPointLoginName(newUser.getLoginName());
        pointChange.setPointCreateTime(DateUtils.getNowDate());
        pointChange.setPointUpdateTime(DateUtils.getNowDate());
        pointChange.setPointSymbol("+");
        pointChangeService.insertPointChange(pointChange);

        newUser.setSign(true);
        newUser.setSignTime(new Date());
        newUser.setSignSort(signTotal + 1);
        newUser.setPoint(newUser.getPoint() + 3);
        userService.updateUserInfo(newUser);

        //更新session
        Message message = new Message();
        message.setSee(0);
        message.setUserId(user.getUserId());
        newUser.setMessageCount(messageService.selectMessageList(message).size());
        session.setAttribute("currentUser", newUser);
        map.put("success", true);
        return map;
    }

    /**
     * 获取当前登录用户是否是VIP用户
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/user/isVip")
    public boolean isVip() {
        User user = ShiroUtils.getSysUser();
        //如果发现vip已过期，则修改用户状态
        if (((user.getVipTime() == null ? new Date().getTime() : user.getVipTime().getTime()) <= new Date().getTime())) {
            User byId = userService.selectUserById(user.getUserId());
            byId.setVipTime(null);
            userService.updateUserInfo(byId);
        }
        //VIP状态正常且在时间段内
        if ((user.getVipTime() == null ? new Date().getTime() : user.getVipTime().getTime()) > new Date().getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 跳转到VIP资源下载页面
     *
     * @param articleId
     * @return
     * @throws Exception
     */
    @RequestMapping("/article/toVipDownLoadPage/{id}")
    public ModelAndView toVipDownLoadPage(@PathVariable("id") Long articleId) {
        UserDownload userDownload = new UserDownload();
        Article article = articleService.selectArticleById(articleId);
        userDownload.setDownloadArticleId(articleId);
        User user = ShiroUtils.getSysUser();
        userDownload.setDownloadUserId(user.getUserId());

        // 判断是否是vip
        if (((user.getVipTime() == null ? new Date().getTime() : user.getVipTime().getTime()) <= new Date().getTime())) {
            return null;
        }

        // 是否下载过
        boolean isDownload = false;
        Integer count = userDownloadService.selectUserDownloadList(userDownload).size();
        if (count > 0) {
            isDownload = true;
        } else {
            isDownload = false;
        }

        if (!isDownload) {
            // 保存用户下载信息
            userDownload.setDownloadArticleId(article.getArticleId());
            userDownload.setDownloadUserId(user.getUserId());
            userDownload.setDownloadDate(new Date());
            userDownloadService.insertUserDownload(userDownload);
        }

        //积分变更记录
        User articleUser = userService.selectUserById(article.getArticleUserId());
        PointChange pointChange2 = new PointChange();
        pointChange2.setPointContent("资源【"+ article.getArticleName()+"】被【"+user.getUserName()+"】下载");
        pointChange2.setPointFront(Long.parseLong(String.valueOf(articleUser.getPoint())));
        pointChange2.setPointEnd(Long.parseLong(String.valueOf(articleUser.getPoint() + article.getArticlePoints())));
        pointChange2.setPointChange(Long.parseLong(String.valueOf(article.getArticlePoints())));
        pointChange2.setPointUserId(articleUser.getUserId());
        pointChange2.setPointStatus(2);
        pointChange2.setPointLoginName(articleUser.getLoginName());
        pointChange2.setPointCreateTime(DateUtils.getNowDate());
        pointChange2.setPointUpdateTime(DateUtils.getNowDate());
        pointChange2.setPointSymbol("+");
        pointChangeService.insertPointChange(pointChange2);

        // 给分享人加积分
        articleUser.setPoint(articleUser.getPoint() + article.getArticlePoints());
        userService.updateUserInfo(articleUser);

        ModelAndView mav = new ModelAndView();
        mav.addObject("article", article);
        mav.setViewName("front/downloadPage");
        return mav;
    }

    @RequestMapping("/user/tobuyVipPage")
    public ModelAndView tobuyVipPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "购买VIP页面");
        mav.setViewName("front/buyVip");
        return mav;
    }

    /**
     * 关键字分词搜索
     *
     * @return
     */
    @RequestMapping("/article/search")
    public ModelAndView search(String q,
                               @RequestParam(value = "deptName", required = false,defaultValue = "") String deptName,
                               @RequestParam(value = "arcTypeStr", required = false,defaultValue = "") String arcTypeStr,
                               @RequestParam(value = "page", required = false) String page, HttpServletRequest request) throws Exception {
        request.getSession().setAttribute("tMenu", "t_0");
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        List<Article> articleList = articleIndex.search(q,deptName,arcTypeStr);
        Integer toIndex = articleList.size() >= Integer.parseInt(page) * 10 ? Integer.parseInt(page) * 10 : articleList.size();
        ModelAndView mav = new ModelAndView();
        // 热门资源
        Article article = new Article();
        startPage(1,43,"article_publish_date desc");
        article.setArticleIsHot(true);
        List<Article> indexHotArticleList = articleService.selectArticleList(article);
        mav.addObject("hotArticleList", indexHotArticleList);
        mav.addObject("title", q);
        mav.addObject("q", q);
        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            //每个资源类别的数量
            List<ArcType> arc_type_list = (List<ArcType>) redisUtil.get("arc_type_list");
            arc_type_list.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                arcType.setAllSize(articleService.selectArticleList(article1).size());
            });
            mav.addObject("allArcTypeList",arc_type_list);
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            redisUtil.set("arc_type_list",arcTypes);
            arcTypes.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                arcType.setAllSize(articleService.selectArticleList(article1).size());
            });
            mav.addObject("allArcTypeList",arcTypes);
        }
        //友情链接
        if (redisUtil.hasKey("link_list")){
            mav.addObject("allLinkList",redisUtil.get("link_list"));
        }else {
            // 从数据库中查询所有友情链接并放入redis
            List<Link> links = linkService.selectLinkList(null);
            redisUtil.set("link_list",links);
            mav.addObject("allLinkList",links);
        }
        mav.addObject("articleList", articleList.subList((Integer.parseInt(page) - 1) * 10, toIndex));
        mav.addObject("resultTotal", articleList.size());
        mav.addObject("pageCode", this.genUpAndDownPageCode(Integer.parseInt(page), articleList.size(), q, 10));
        mav.setViewName("front/result");
        return mav;
    }

    /**
     * 生成上一页，下一页代码
     *
     * @param page
     * @param totalNum
     * @param q
     * @param pageSize
     * @return
     */
    private String genUpAndDownPageCode(Integer page, Integer totalNum, String q, Integer pageSize) {
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        StringBuffer pageCode = new StringBuffer();
        if (totalPage == 0) {
            return "";
        } else {
            pageCode.append("<div class='layui-box layui-laypage layui-laypage-default'>");
            if (page > 1) {
                pageCode.append("<a href='/article/search?page=" + (page - 1) + "&q=" + q + "' class='layui-laypage-prev'>上一页</a>");
            } else {
                pageCode.append("<a href='#' class='layui-laypage-prev layui-disabled'>上一页</a>");
            }
            if (page < totalPage) {
                pageCode.append("<a href='/article/search?page=" + (page + 1) + "&q=" + q + "' class='layui-laypage-next'>下一页</a>");
            } else {
                pageCode.append("<a href='#' class='layui-laypage-next layui-disabled'>下一页</a>");
            }
            pageCode.append("</div>");
        }
        return pageCode.toString();
    }

    /**
     * 清除今日签到信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/removeSign")
    @RequiresPermissions("system:user:removeSign")
    public ModelAndView removeSign() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            userService.updateAllSignInfo();
            if (redisUtil.hasKey("signTotal")){
                redisUtil.delete("signTotal");
            }
            redisUtil.set("signTotal",0);
            modelAndView.addObject("status",true);
        }catch (Exception e){
            modelAndView.addObject("status",false);
        }
        modelAndView.setViewName("common/removeSign");
        return modelAndView;
    }

    /**
     * 获取励志语接口
     * @return
     */
    @GetMapping("/getIns")
    @ResponseBody
    public AjaxResult getIns(){
        RestTemplate restTemplate = new RestTemplate();
        String ins = restTemplate.getForObject("https://www.qqzsh.top/getIns"
                , String.class);
        if (StringUtils.isNotEmpty(ins)){
            return AjaxResult.success(ins);
        }else {
            return AjaxResult.error();
        }
    }

    /**
     * 打开精品资源页面
     * @return
     */
    @GetMapping("/boutique")
    public ModelAndView boutique(HttpServletRequest request){
        request.getSession().setAttribute("tMenu", "t_0");
        ModelAndView data = getData(null, 1, "");
        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            //每个资源类别的数量
            List<ArcType> arc_type_list = (List<ArcType>) redisUtil.get("arc_type_list");
            arc_type_list.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                article1.setArticleIsHot(true);
                arcType.setHotSize(articleService.selectArticleList(article1).size());
            });
            data.addObject("allArcTypeList",arc_type_list);
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            redisUtil.set("arc_type_list",arcTypes);
            arcTypes.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                article1.setArticleIsHot(true);
                arcType.setHotSize(articleService.selectArticleList(article1).size());
            });
            data.addObject("allArcTypeList",arcTypes);
        }
        Article article = new Article();
        // 审核通过的帖子
        article.setArticleState(1L);
        // 热门帖子，精品资源
        article.setArticleIsHot(true);
        startPage(1,20,"article_publish_date desc");
        List<Article> list = articleService.selectArticleList(article);
        List<Article> newList = new LinkedList<>();
        list.forEach(article1 -> {
            article1.setUser(userService.selectUserById(article1.getArticleUserId()));
            newList.add(article1);
        });
        data.addObject("articleList", newList);
        // 获取总共多少页
        data.addObject("pageCode", PageUtil.genPagination("/article/list/boutique", new PageInfo(list).getTotal(), 1, 20, ""));
        data.setViewName("front/boutique");
        // 签到总数
        if (redisUtil.hasKey("signTotal")){
            data.addObject("signTotal", redisUtil.get("signTotal"));
        }else {
            redisUtil.set("signTotal", 0);
            data.addObject("signTotal", 0);
        }
        return data;
    }

    /**
     * 精品资源分页
     * @param typeId
     * @param page
     * @param request
     * @return
     */
    @GetMapping("/article/list/boutique/{id}")
    public ModelAndView boutique(@RequestParam(value = "typeId", required = false) Long typeId,
                                 @PathVariable(value = "id", required = false) Integer page,
                                 HttpServletRequest request){
        StringBuffer param = new StringBuffer();
        if (typeId != null) {
            param.append("?typeId=" + typeId);
        }
        if (typeId != null) {
            request.getSession().setAttribute("tMenu", "t_" + typeId);
        }
        ModelAndView data = getData(typeId, page,param.toString());
        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            //每个资源类别的数量
            List<ArcType> arc_type_list = (List<ArcType>) redisUtil.get("arc_type_list");
            arc_type_list.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                article1.setArticleIsHot(true);
                arcType.setHotSize(articleService.selectArticleList(article1).size());
            });
            data.addObject("allArcTypeList",arc_type_list);
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            redisUtil.set("arc_type_list",arcTypes);
            arcTypes.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                article1.setArticleIsHot(true);
                arcType.setHotSize(articleService.selectArticleList(article1).size());
            });
            data.addObject("allArcTypeList",arcTypes);
        }
        Article article = new Article();
        // 审核通过的帖子
        article.setArticleState(1L);
        // 热门
        article.setArticleIsHot(true);
        if (page == null){
            page = 1;
        }
        if (typeId != null) {
            ArcType arcType = arcTypeService.selectArcTypeById(typeId);
            article.setArticleTypeId(typeId);
            data.addObject("title", arcType.getSrcTypeName() + "-第" + page + "页");
        }else {
            data.addObject("title", "第" + page + "页");
        }
        startPage(page,20,"article_publish_date desc");
        List<Article> list = articleService.selectArticleList(article);
        List<Article> newList = new LinkedList<>();
        list.forEach(article1 -> {
            article1.setUser(userService.selectUserById(article1.getArticleUserId()));
            newList.add(article1);
        });
        data.addObject("articleList", newList);
        // 获取总共多少页
        data.addObject("pageCode", PageUtil.genPagination("/article/list/boutique", new PageInfo(list).getTotal(), page, 20, param.toString()));
        data.setViewName("front/boutique");
        return data;
    }

    /**
     * 打开通知页面
     * @return
     */
    @GetMapping("/notice")
    public ModelAndView notice(){
        ModelAndView data = getData(null, 1, "");
        Notice notice = new Notice();
        // 状态正常
        notice.setStatus("0");
        // 类型为通知
        notice.setNoticeType("1");
        startPage(1,20,"create_time desc");
        List<Notice> list = noticeService.selectNoticeList(notice);
        data.addObject("noticeList", list);
        // 获取总共多少页
        data.addObject("pageCode", PageUtil.genPagination("/notice/list", new PageInfo(list).getTotal(), 1, 20, ""));
        data.setViewName("front/notice");
        // 签到总数
        if (redisUtil.hasKey("signTotal")){
            data.addObject("signTotal", redisUtil.get("signTotal"));
        }else {
            redisUtil.set("signTotal", 0);
            data.addObject("signTotal", 0);
        }
        return data;
    }

    /**
     * 通知公告分页
     */
    @GetMapping("/notice/list/{id}")
    public ModelAndView noticelist(@PathVariable(value = "id", required = false) Integer page){
        ModelAndView data = getData(null, page, "");
        Notice notice = new Notice();
        // 状态正常
        notice.setStatus("0");
        // 类型为通知
        notice.setNoticeType("1");
        startPage(page,20,"create_time desc");
        List<Notice> list = noticeService.selectNoticeList(notice);
        data.addObject("noticeList", list);
        // 获取总共多少页
        data.addObject("pageCode", PageUtil.genPagination("/notice/list", new PageInfo(list).getTotal(), page, 20, ""));
        data.setViewName("front/notice");
        return data;
    }

    /**
     * 根据id查询通知
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/notice/detail/{id}")
    public ModelAndView noticeView(@PathVariable("id") Long id) throws Exception {
        ModelAndView data = getData(null, null, "");
        Notice notice = noticeService.selectNoticeById(id);
        data.addObject("notice",notice);
        // 签到总数
        if (redisUtil.hasKey("signTotal")){
            data.addObject("signTotal", redisUtil.get("signTotal"));
        }else {
            redisUtil.set("signTotal", 0);
            data.addObject("signTotal", 0);
        }
        data.addObject("title",notice.getNoticeTitle());
        data.setViewName("front/noticeDetail");
        return data;
    }

    @GetMapping("/about")
    public ModelAndView about(){
        ModelAndView data = getData(null, 1, "");
        Notice notice = new Notice();
        // 状态正常
        notice.setStatus("0");
        // 类型为平台简介
        notice.setNoticeType("3");
        startPage(1,20,"create_time desc");
        List<Notice> list = noticeService.selectNoticeList(notice);
        if (list.size() != 0){
            data.addObject("about", list.get(0).getNoticeContent());
        }else {
            data.addObject("about", "暂无简介");
        }
        data.setViewName("front/about");
        return data;
    }


}
