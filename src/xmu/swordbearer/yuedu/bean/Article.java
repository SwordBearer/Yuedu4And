package xmu.swordbearer.yuedu.bean;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-11-03.
 */
public class Article extends BaseBean {
    private int id;//0 存放在sqlite的ID
    private int cloudId;//1 //存放在云端数据库的ID，两者很有可能不相同
    private String title;//2 标题
    private String author;//  作者
    private String source;//4 来源
    private String outline;//  简介
    private long birth;//6 发布日期
    private String content;//7 文章内容

    public Article() {
    }

    public Article(Cursor cursor) {
        id = cursor.getInt(0);
        cloudId = cursor.getInt(1);
        title = cursor.getString(2);
        author = cursor.getString(3);
        source = cursor.getString(4);
        outline = cursor.getString(5);
        birth = cursor.getLong(6);
        content = cursor.getString(7);
    }

    public static Article parseJSON(JSONObject jo) throws JSONException {
        Article article = new Article();
        article.cloudId = jo.getInt("id");
        article.title = jo.getString("title");
        if (jo.has("author")) {
            article.author = jo.getString("author");
        }
        if (jo.has("source")) {
            article.source = jo.getString("source");
        }
        if (jo.has("outline")) {
            article.outline = jo.getString("outline");
        }
        if (jo.has("content")) {
            article.content = jo.getString("content");
        }
        article.birth = jo.getLong("birth");
        return article;
    }

    public static class ArticleColumns implements BaseColumns {
        public static final String _CLOUD_ID = "cloudId";
        public static final String _TITLE = "title";
        public static final String _AUTHOR = "author";
        public static final String _SOURCE = "source";
        public static final String _OUTLINE = "outline";
        public static final String _BIRTH = "birth";
        public static final String _CONTENT = "content";
    }
//
//    public String generateCacheKey() {
//        return "yuedu_cache_article_" + this.id;
//    }

    public int getId() {
        return id;
    }

    public int getCloudId() {
        return cloudId;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    public long getBirth() {
        return birth;
    }

    public String getOutline() {
        return outline;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(int id) {
        this.id = id;
    }


}