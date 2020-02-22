package qqzsh.top.preparation.project.monitor.job.task;

import me.zhyd.hunter.config.HunterConfig;
import me.zhyd.hunter.config.HunterConfigContext;
import me.zhyd.hunter.config.platform.Platform;
import me.zhyd.hunter.entity.VirtualArticle;
import me.zhyd.hunter.enums.ExitWayEnum;
import me.zhyd.hunter.processor.BlogHunterProcessor;
import me.zhyd.hunter.processor.HunterProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.framework.web.service.ConfigService;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.project.content.virtualarticle.domain.Virtualarticle;
import qqzsh.top.preparation.project.content.virtualarticle.service.IVirtualarticleService;
import qqzsh.top.preparation.project.system.dict.domain.DictData;
import qqzsh.top.preparation.project.system.dict.service.IDictDataService;
import qqzsh.top.preparation.project.system.user.service.IUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-02-22 15:47
 * @description 博客爬虫
 */
@Component("CrawlerBlog")
public class CrawlerBlog {

    @Autowired
    private IDictDataService dictDataService;

    @Autowired
    private IVirtualarticleService virtualarticleService;

    @Autowired
    private IArcTypeService arcTypeService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ConfigService configService;

    /**
     * 采集方法
     */
    public void start(){
        // 判断采集方式
        String key = configService.getKey("crawler.blog.type");
        ExitWayEnum exitWayEnum = null;
        if ("全量".equalsIgnoreCase(key)){
            exitWayEnum = ExitWayEnum.DEFAULT;
        }else if ("增量".equalsIgnoreCase(key)){
            exitWayEnum = ExitWayEnum.URL_COUNT;
        }
        //设置平台及数据字段
        String[] fields = {"imooc.list","csdn.list","cnblogs.list","juejin.list","v2ex.list"};
        Platform[] platforms = {Platform.IMOOC,Platform.CSDN,Platform.CNBLOGS,Platform.JUEJIN,Platform.V2EX};
        for (int i = 0; i <fields.length ; i++) {
            crawler(exitWayEnum,platforms[i],fields[i]);
        }
    }

    /**
     * 采集方法
     * @param exitWayEnum 退出方式 全量 增量
     * @param platform 采集平台
     * @param dictionary 数据字段
     */
    public void crawler(ExitWayEnum exitWayEnum,Platform platform,String dictionary){
        Long typeId = null;
        Long userId = null;
        ArcType arcType = new ArcType();
        arcType.setSrcTypeName("其他");
        List<ArcType> arcTypes = arcTypeService.selectArcTypeList(arcType);
        typeId = arcTypes.get(0).getSrcTypeId();
        userId = userService.selectUserByEmail("pcyh@qqzsh.top").getUserId();
        //1.获取数据字典的value
        List<DictData> dictData = dictDataService.selectDictDataByType(dictionary);
        List<String> strings = new ArrayList<>();
        dictData.forEach(dict -> {
            if ("userid".equals(dict.getDictLabel())){
                strings.add(dict.getDictValue());
            }
        });
        //2.爬取数据
        Long finalTypeId = typeId;
        Long finalUserId = userId;
        strings.forEach(s -> {
            HunterConfig config = HunterConfigContext.getHunterConfig(platform);
            // set会重置，add会追加
            config.setUid(s)
                    // 设置程序退出的方式
                    .setExitWay(exitWayEnum)
                    // 设定抓取120秒， 如果所有文章都被抓取过了，则会提前停止
                    .setCount(20)
                    // 每次抓取间隔的时间
                    .setSleepTime(100)
                    // 失败重试次数
                    .setRetryTimes(3)
                    // 针对抓取失败的链接 循环重试次数
                    .setCycleRetryTimes(3)
                    // 开启的线程数
                    .setThreadCount(5)
                    // 开启图片转存
                    .setConvertImg(true);
            HunterProcessor hunter = new BlogHunterProcessor(config);
            CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
            list.forEach(vir -> {
                Virtualarticle virtualarticle = new Virtualarticle();
                virtualarticle.setAuthor(vir.getAuthor());
                virtualarticle.setContent(vir.getContent());
                virtualarticle.setDescription(vir.getDescription());
                virtualarticle.setKeywords(vir.getKeywords());
                virtualarticle.setReleasedate(vir.getReleaseDate());
                virtualarticle.setSource(vir.getSource());
                virtualarticle.setTitle(vir.getTitle());
                virtualarticle.setImagelinks(StringUtils.join(vir.getImageLinks().toArray(),";"));
                virtualarticle.setTags(StringUtils.join(vir.getTags().toArray(), ";"));

                //3.保存
                Virtualarticle v = new Virtualarticle();
                v.setSource(vir.getSource());
                List<Virtualarticle> virtualarticles = virtualarticleService.selectVirtualarticleList(v);
                if (virtualarticles.size() == 0){
                    virtualarticleService.insertVirtualarticle(virtualarticle);
                    //发布资源
                    Article article = new Article();
                    article.setArticlePublishDate(new Date());
                    article.setArticleContent(virtualarticle.getContent());
                    article.setArticleDownload1(virtualarticle.getSource());
                    article.setArticlePassword1("无");
                    article.setArticleIsHot(false);
                    article.setArticleName(virtualarticle.getTitle());
                    article.setArticlePoints(0);
                    article.setArticleCheckDate(virtualarticle.getReleasedate());
                    article.setArticleState(1L);
                    article.setArticleView(0L);
                    article.setArticleTypeId(finalTypeId);
                    article.setArticleUserId(finalUserId);
                    try {
                        articleService.insertArticle(article);
                    } catch (Exception e) { }
                }
            });
        });
    }

}
