package qqzsh.top.preparation.controller.admin;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import qqzsh.top.preparation.entity.Message;
import qqzsh.top.preparation.entity.User;
import qqzsh.top.preparation.lucene.ArticleIndex;
import qqzsh.top.preparation.service.ArticleService;
import qqzsh.top.preparation.service.CommentService;
import qqzsh.top.preparation.service.MessageService;
import qqzsh.top.preparation.service.UserDownloadService;
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
 * @create 2019-09-24 21:09
 * @Description 管理员-资源控制器
 */
@Controller
@RequestMapping("/admin/article")
public class ArticleAdminController {

    @Autowired
    private ArticleService articleService;

    @Value("${articleImageFilePath}")
    private String articleImageFilePath;

    @Autowired
    private ArticleIndex articleIndex;

    @Value("${lucenePath}")
    private String lucenePath;

    @Autowired
    private UserDownloadService userDownloadService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisUtil<Article> redisUtil;


    /**
     * 生成所有帖子索引
     * @return
     */
    @ResponseBody
    @RequestMapping(value="genAllIndex")
    @RequiresPermissions(value={"生成所有帖子索引"})
    public boolean genAllIndex(){
        //1.删除原来的索引
        try {
            File file = new File(lucenePath);
            if (file.isDirectory()){
                for (File f : file.listFiles()){
                    f.delete();
                }
            }
        }catch (Exception e){
            return false;
        }
        //2.添加新的索引
        List<Article> articleList = articleService.listAll();
        for(Article article:articleList){
            if(!articleIndex.addIndex(article)){
                return false;
            }
        }
        return true;
    }



    /**
     * 分页查询资源帖子信息
     * @param s_article
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value={"分页查询资源帖子信息"})
    @RequestMapping("/list")
    public Map<String,Object> list(Article s_article, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="limit",required=false)Integer limit)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        List<Article> articleList = articleService.list(s_article,page, limit, Sort.Direction.DESC, "publishDate");
        Long total = articleService.getTotal(s_article);
        resultMap.put("code", 0);
        resultMap.put("count", total);
        resultMap.put("data", articleList);
        return resultMap;
    }

    /**
     * 跳转到帖子审核页面
     * @param id
     * @return
     */
    @RequestMapping("/toReViewArticlePage/{id}")
    @RequiresPermissions(value={"跳转到帖子审核页面"})
    public ModelAndView toReViewArticlePage(@PathVariable("id")Integer id){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "审核帖子页面");
        mav.addObject("article", articleService.get(id));
        mav.setViewName("admin/reviewArticle");
        return mav;
    }

    /**
     * 跳转到修改帖子页面
     * @param id
     * @return
     */
    @RequestMapping("/toModifyArticlePage/{id}")
    @RequiresPermissions(value={"跳转到修改帖子页面"})
    public ModelAndView toModifyArticlePage(@PathVariable("id")Integer id){
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "修改帖子页面");
        mav.addObject("article", articleService.get(id));
        mav.setViewName("admin/modifyArticle");
        return mav;
    }

    /**
     * Layui编辑器图片上传处理
     * @param file
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadImage")
    @RequiresPermissions(value={"图片上传"})
    public Map<String,Object> uploadImage(MultipartFile file)throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();
        if(!file.isEmpty()){
            String fileName=file.getOriginalFilename(); // 获取文件名
            String suffixName=fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
            String newFileName= DateUtil.getCurrentDateStr()+suffixName; // 新文件名
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(articleImageFilePath+DateUtil.getCurrentDatePath()+newFileName));
            map.put("code", 0);
            map.put("msg", "上传成功");
            Map<String,Object> map2=new HashMap<String,Object>();
            map2.put("src", "/image/"+DateUtil.getCurrentDatePath()+newFileName);
            map2.put("title", newFileName);
            map.put("data", map2);
        }
        return map;
    }

    /**
     * 修改帖子
     * @param article
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    @RequiresPermissions(value={"修改帖子"})
    public ModelAndView update(Article article)throws Exception{
        Article oldArticle = articleService.get(article.getId());
        oldArticle.setName(article.getName());
        oldArticle.setArcType(article.getArcType());
        oldArticle.setContent(article.getContent());
        oldArticle.setDownload1(article.getDownload1());
        oldArticle.setPassword1(article.getPassword1());
        oldArticle.setPoints(article.getPoints());
        if(oldArticle.getState()==2){ // 当审核通过的时候，需要更新下lucene索引
            //更新lucene索引
            articleIndex.updateIndex(oldArticle);
        }
        articleService.save(oldArticle);
        //删除该帖子的缓存、待再次点击重新生成
        redisUtil.del("article_"+article.getId());
        ModelAndView mav=new ModelAndView();
        mav.addObject("title", "修改帖子成功页面");
        mav.setViewName("admin/modifyArticleSuccess");
        return mav;
    }

    /**
     * 修改状态
     * @param article
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value={"修改状态"})
    @RequestMapping("/updateState")
    public Map<String,Object> updateState(Article article,HttpSession session)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        Article oldArticle=articleService.get(article.getId());
        //消息模块添加
        Message message=new Message();
        message.setUser(oldArticle.getUser());
        message.setPublishDate(new Date());
        if(article.getState()==2){
            oldArticle.setState(2);
            articleIndex.addIndex(oldArticle);
            message.setContent("【审核通过】您发布的【"+oldArticle.getName()+"】帖子审核成功！");
            //删除redis 首页数据缓存
            redisUtil.del("article_"+article.getId());
        }else{
            oldArticle.setState(3);
            oldArticle.setReason(article.getReason());
            message.setContent("【审核失败】您发布的【"+oldArticle.getName()+"】帖子审核未成功，原因是："+article.getReason());
        }
        //如何当前用户是消息的接收者,修改未读条数
        User user=(User)session.getAttribute("currentUser");
        //设置未读条数+1
        user.setMessageCount(user.getMessageCount()+1);
        //更新session用户
        session.setAttribute("currentUser", user);

        articleService.save(oldArticle);
        messageService.save(message);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 根据id删除帖子
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(value={"删除帖子"})
    public Map<String,Object> delete(Integer id)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        //删除用户下载帖子信息
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
     * @param ids
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/deleteSelected")
    @RequiresPermissions(value={"删除帖子"})
    public Map<String,Object> deleteSelected(String ids)throws Exception{
        String[] idsStr = ids.split(",");
        for(int i=0;i<idsStr.length;i++){
            // 删除用户下载帖子信息
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
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 修改热门状态
     * @param article
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/updateHotState")
    @RequiresPermissions(value={"修改热门状态"})
    public Map<String,Object> updateHotState(Article article)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        Article oldArticle = articleService.get(article.getId());
        oldArticle.setHot(article.isHot());
        articleService.save(oldArticle);
        //删除该帖子对应的redis索引
        redisUtil.del("article_"+oldArticle.getId());
        resultMap.put("success", true);
        return resultMap;
    }

}

