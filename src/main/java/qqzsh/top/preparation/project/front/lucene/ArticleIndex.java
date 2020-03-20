package qqzsh.top.preparation.project.front.lucene;

import com.github.pagehelper.util.StringUtil;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.framework.config.PreparationConfig;
import qqzsh.top.preparation.project.content.article.domain.Article;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2020-01-02 10:14
 * @description lucene配置
 */
@Component("articleIndex")
public class ArticleIndex {

    private Directory dir = null;

    /**
     * 默认lucene的地址
     */
    private static String lucenePath = PreparationConfig.getProfile() + "/lucene/";

    /**
     * 获取IndexWriter实例
     *
     * @return
     * @throws Exception
     */
    public IndexWriter getWriter() throws Exception {
        dir = FSDirectory.open(Paths.get(lucenePath));
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, iwc);
        return writer;
    }

    /**
     * 添加帖子索引
     *
     * @param article
     */
    public boolean addIndex(Article article) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            IndexWriter writer = getWriter();
            Document doc = new Document();
            // 资源ID
            doc.add(new StringField("articleId", String.valueOf(article.getArticleId()), Field.Store.YES));
            // 资源名称
            doc.add(new TextField("articleName", article.getArticleName(), Field.Store.YES));
            // 发布时间
            doc.add(new StringField("articlePublishDate", DateUtils.formatDate(article.getArticlePublishDate()), Field.Store.YES));
            // 资源内容
            doc.add(new TextField("articleContent", article.getArticleContent(), Field.Store.YES));
            // 所属高校
            doc.add(new TextField("deptName", article.getDept().getDeptName(), Field.Store.YES));
            // 资源类别
            doc.add(new TextField("arcType", article.getArcType().getSrcTypeName(), Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 更新帖子索引
     *
     * @param article
     */
    public boolean updateIndex(Article article) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            IndexWriter writer = getWriter();
            Document doc = new Document();
            // 资源ID
            doc.add(new StringField("articleId", String.valueOf(article.getArticleId()), Field.Store.YES));
            // 资源名称
            doc.add(new TextField("articleName", article.getArticleName(), Field.Store.YES));
            // 发布时间
            doc.add(new StringField("articlePublishDate", DateUtils.formatDate(article.getArticlePublishDate()), Field.Store.YES));
            // 资源内容
            doc.add(new TextField("articleContent", article.getArticleContent(), Field.Store.YES));
            // 所属高校
            doc.add(new TextField("deptName", article.getDept().getDeptName(), Field.Store.YES));
            // 资源类别
            doc.add(new TextField("arcType", article.getArcType().getSrcTypeName(), Field.Store.YES));
            writer.updateDocument(new Term("articleId", String.valueOf(article.getArticleId())), doc);
            writer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 删除帖子索引
     *
     * @param id
     */
    public void deleteIndex(String id) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            IndexWriter writer = getWriter();
            writer.deleteDocuments(new Term("articleId", id));
            writer.forceMergeDeletes(); // 强制删除
            writer.commit();
            writer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 分词搜索帖子信息 高亮 100条
     *
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    public List<Article> search(String q,String deptName,String arcType) throws Exception {
        dir = FSDirectory.open(Paths.get(lucenePath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        QueryParser parser = new QueryParser("articleName", analyzer);
        Query query = parser.parse(q);
        QueryParser parser2 = new QueryParser("articleContent", analyzer);
        Query query2 = parser2.parse(q);
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
        if (StringUtils.isNotBlank(deptName) || StringUtils.isNotBlank(arcType)){
            if (StringUtils.isNotBlank(deptName)){
                QueryParser parser3 = new QueryParser("deptName", analyzer);
                Query query3 = parser3.parse(deptName);
                booleanQuery.add(query3, BooleanClause.Occur.MUST);
            }
            if (StringUtils.isNotBlank(arcType)){
                QueryParser parser4 = new QueryParser("arcType", analyzer);
                Query query4 = parser4.parse(arcType);
                booleanQuery.add(query4, BooleanClause.Occur.MUST);
            }
        }
        TopDocs hits = is.search(booleanQuery.build(), 100);
        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        List<Article> articleList = new LinkedList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            Article article = new Article();
            article.setArticleId(Long.parseLong(doc.get(("articleId"))));
            article.setArticlePublishDate(DateUtils.parseDate(doc.get(("articlePublishDate"))));
            String name = doc.get("articleName");
            String content = StringUtils.stripHtml(doc.get("articleContent"));
            if (name != null) {
                TokenStream tokenStream = analyzer.tokenStream("articleName", new StringReader(name));
                String hName = highlighter.getBestFragment(tokenStream, name);
                if (StringUtil.isEmpty(hName)) {
                    article.setArticleName(name);
                } else {
                    article.setArticleName(hName);
                }
            }
            if (content != null) {
                TokenStream tokenStream = analyzer.tokenStream("articleContent", new StringReader(content));
                String hContent = highlighter.getBestFragment(tokenStream, content);
                if (StringUtil.isEmpty(hContent)) {
                    if (content.length() <= 200) {
                        article.setArticleContent(content);
                    } else {
                        article.setArticleContent(content.substring(0, 200));
                    }
                } else {
                    article.setArticleContent(hContent);
                }
            }
            articleList.add(article);
        }
        return articleList;
    }

    /**
     * 查询帖子信息无高亮 20条
     *
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    public List<Article> searchNoHighLighter(String q) throws Exception {
        dir = FSDirectory.open(Paths.get(lucenePath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        QueryParser parser = new QueryParser("articleName", analyzer);
        Query query = parser.parse(q);
        QueryParser parser2 = new QueryParser("articleContent", analyzer);
        Query query2 = parser2.parse(q);
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
        TopDocs hits = is.search(booleanQuery.build(), 20);
        List<Article> articleList = new LinkedList<Article>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            Article article = new Article();
            article.setArticleId(Long.parseLong(doc.get(("articleId"))));
            String name = doc.get("articleName");
            article.setArticleName(name);
            articleList.add(article);
        }
        return articleList;
    }
}


