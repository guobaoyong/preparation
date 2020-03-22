package qqzsh.top.preparation.project.monitor.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qqzsh.top.preparation.common.utils.RedisUtil;
import qqzsh.top.preparation.project.content.article.domain.Article;
import qqzsh.top.preparation.project.content.article.service.IArticleService;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;

import java.util.List;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-02-23 9:23
 * @description 定期更新 每隔半个小时更新一次
 * 各个类别资源总数
 */
@Component("UpdateNumber")
public class UpdateNumber {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IArcTypeService arcTypeService;

    @Autowired
    private RedisUtil redisUtil;

    public void update(){
        // 将资源类别返回前台
        if (redisUtil.hasKey("arc_type_list")){
            //每个资源类别的数量
            List<ArcType> arc_type_list = (List<ArcType>) redisUtil.get("arc_type_list");
            arc_type_list.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                arcType.setAllSize(articleService.selectArticleListCount(article1));
            });
            redisUtil.delete("arc_type_list");
            redisUtil.set("arc_type_list",arc_type_list);
        }else {
            // 从数据库中查询所有资源类别并放入redis
            List<ArcType> arcTypes = arcTypeService.selectArcTypeList(null);
            redisUtil.set("arc_type_list",arcTypes);
            arcTypes.forEach(arcType -> {
                Article article1 = new Article();
                article1.setArticleState(1L);
                article1.setArticleTypeId(arcType.getSrcTypeId());
                arcType.setAllSize(articleService.selectArticleListCount(article1));
            });
            redisUtil.set("arc_type_list",arcTypes);
        }
    }
}
