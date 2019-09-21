package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.Article;
import qqzsh.top.preparation.respository.ArticleRepository;
import qqzsh.top.preparation.service.ArticleService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 16:41
 * @description 资源Service实现类
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> list(Article s_article, Integer page, Integer pageSize, Sort.Direction direction,
                              String... properties) {
        Pageable pageable=new PageRequest(page-1, pageSize, direction, properties);
        Page<Article> pageArticle = articleRepository.findAll(new Specification<Article>() {

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_article!=null){
                    if(s_article.getState()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("state"), s_article.getState()));
                    }
                    if(s_article.isHot()){
                        predicate.getExpressions().add(cb.equal(root.get("isHot"), 1));
                    }
                    if(s_article.getArcType()!=null && s_article.getArcType().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("arcType").get("id"), s_article.getArcType().getId()));
                    }
                    if(!s_article.isUseful()){
                        predicate.getExpressions().add(cb.equal(root.get("isUseful"),false));
                    }
                    if(s_article.getUser()!=null && s_article.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), s_article.getUser().getId()));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageArticle.getContent();
    }

    @Override
    public Long getTotal(Article s_article) {
        Long count=articleRepository.count(new Specification<Article>() {

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_article!=null){
                    if(s_article.getState()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("state"), s_article.getState()));
                    }
                    if(s_article.isHot()){
                        predicate.getExpressions().add(cb.equal(root.get("isHot"), 1));
                    }
                    if(s_article.getArcType()!=null && s_article.getArcType().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("arcType").get("id"), s_article.getArcType().getId()));
                    }
                    if(!s_article.isUseful()){
                        predicate.getExpressions().add(cb.equal(root.get("isUseful"),false));
                    }
                    if(s_article.getUser()!=null && s_article.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"), s_article.getUser().getId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public Article get(Integer id) {
        return articleRepository.findOne(id);
    }

    @Override
    public void save(Article article) {
        articleRepository.save(article);
    }
}

