package xmu.swordbearer.yuedu.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SwordBearer on 13-8-11.
 */
public class Article extends BaseBean {
    private long id;
    private String title;
    private String author;
    private String source;
    private String outline;
    private String birth;
    private String content;

    public static Article parseJSON(JSONObject jo) throws JSONException {
        Article article = new Article();
        article.id = jo.getLong("id");
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
        article.birth = jo.getString("birth");
        return article;
    }

    public String generateCacheKey() {
        return "yuedu_cache_article_" + this.id;
    }

    public long getId() {
        return id;
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

    public String getBirth() {
        return birth;
    }

    public String getOutline() {
        return outline;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(long id) {
        this.id = id;
    }


}